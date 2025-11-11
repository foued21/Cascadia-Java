package fr.uge.cascadia.game;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import com.github.forax.zen.ApplicationContext;

import fr.uge.cascadia.score.ScoreCalculator;
import fr.uge.cascadia.score.SuccessManager;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.FauneBag;
import fr.uge.cascadia.structure.GameLot;
import fr.uge.cascadia.structure.Habitat;
import fr.uge.cascadia.structure.Tile;
import fr.uge.cascadia.structure.TileAnimalPair;
import fr.uge.cascadia.structure.Tiles;
import fr.uge.cascadia.view.GameViewHex;
import fr.uge.cascadia.view.GameViewSquare;

/**
 * Represents a player in the Cascadia game, responsible for placing tiles and
 * animals on their board and calculating scores.
 *
 * @param name  the name of the player
 * @param board the player's game board
 * @throws NullPointerException if {@code name} or {@code board} is {@code null}
 */
public class Player {
	private final String name;
	private final Environnement board;
	private final ScoreCalculator scoreCalculator;
	private final SuccessManager successManager;
	private int natureTokens;

	/**
	 * Constructs a new {@code Player}.
	 *
	 * @param name  the name of the player
	 * @param board the player's game board
	 * @throws NullPointerException if {@code name} or {@code board} is {@code null}
	 */
	public Player(String name, Environnement board, SuccessManager successManager) {
		this.name = Objects.requireNonNull(name);
		this.board = Objects.requireNonNull(board);
		this.successManager = Objects.requireNonNull(successManager);
		this.natureTokens = 0;
		this.scoreCalculator = new ScoreCalculator(this);
	}

	/**
	 * Returns the name of the player.
	 *
	 * @return the name of the player
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the player's game board.
	 *
	 * @return the player's game board
	 */
	public Environnement getBoard() {
		return board;
	}

	/**
	 * Returns the player's nature tokens.
	 *
	 * @return the player's game board
	 */
	public int getNatureTokens() {
		return natureTokens;
	}

	public void addNatureToken() {
		natureTokens++;
	}

	public void removeNatureToken() {
		natureTokens--;
	}

	public ScoreCalculator getScoreCalculator() {
		return scoreCalculator;
	}

	/**
	 * Allows the player to choose a {@link TileAnimalPair} from the available lots.
	 *
	 * @param availableLot the current available lot of tiles and animals
	 * @return the chosen {@link TileAnimalPair}
	 * @throws NullPointerException if {@code availableLot} is {@code null}
	 */
	public TileAnimalPair chooseLot(GameLot availableLot) {
		Objects.requireNonNull(availableLot);
		@SuppressWarnings("resource")
		var scanner = new Scanner(System.in);
		while (true) {
			try {
				int choice = Integer.parseInt(scanner.next());
				if (choice >= 1 && choice <= 4) {
					return availableLot.get(choice - 1);
				} else {
					System.out.println("Invalid choice. Please select a number between 1 and 4.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number between 1 and 4.");
			}
		}
	}

	/**
	 * Validates if the input coordinates are valid positive integers.
	 *
	 * @param sx the input string for the x-coordinate
	 * @param sy the input string for the y-coordinate
	 * @return {@code true} if the inputs are valid coordinates, {@code false}
	 *         otherwise
	 * @throws NullPointerException if {@code sx or sy} is {@code null}
	 */
	public boolean validInput(String sx, String sy) {
		Objects.requireNonNull(sx);
		Objects.requireNonNull(sy);
		try {
			int x = Integer.parseInt(sx);
			int y = Integer.parseInt(sy);
			return x >= 0 && y >= 0; // checks if x and y are positif
		} catch (NumberFormatException e) {
			return false; // returns false if there is an exception
		}
	}

	/**
	 * Allows the player to place a tile on the board.
	 *
	 * @param tile the tile to place
	 * @throws NullPointerException if {@code tile} is {@code null}
	 */
	@SuppressWarnings("resource")
	public void tilePlacing(Tile tile) {
		Objects.requireNonNull(tile);
		Scanner scanner = new Scanner(System.in);
		String xInput, yInput;
		int x, y, success = 0;
		while (success == 0) {
			System.out.println("Enter the coordinates to place the tile (x y) :");
			while (true) {
				xInput = scanner.next();
				yInput = scanner.next();
				if (validInput(xInput, yInput)) {
					x = Integer.parseInt(xInput);
					y = Integer.parseInt(yInput);
					break;
				} else {
					System.out.println("Invalid input. Please enter two positive numbers.");
				}
			}
			success = board.placeTile(tile, x, y);
			if (success == 2) {
				natureTokens++;
			}
		}
	}

	/**
	 * Allows the player to place an animal on the board.
	 *
	 * @param animal the animal to place
	 * @throws NullPointerException if {@code animal} is {@code null}
	 */
	@SuppressWarnings("resource")
	public void animalPlacing(Animal animal) {
		Objects.requireNonNull(animal);
		Scanner scanner = new Scanner(System.in);
		String xInput, yInput;
		int x, y;
		int success = 0;
		while (success != 1) {
			System.out.println("Enter the coordinates to place the animal (x y) :");
			while (true) {
				xInput = scanner.next();
				yInput = scanner.next();
				if (validInput(xInput, yInput)) {
					x = Integer.parseInt(xInput);
					y = Integer.parseInt(yInput);
					break;
				} else {
					System.out.println("Invalid input. Please enter two positive numbers.");
				}
			}
			success = board.placeAnimal(animal, x, y);
		}
	}

	/**
	 * Executes the player's turn, where they choose a tile and place it along with
	 * an animal.
	 *
	 * @param availableLot the available lot of tiles and animals
	 * @param animals      the {@link FauneBag} containing all animals in the game
	 * @return the chosen {@link TileAnimalPair}
	 * @throws NullPointerException if {@code availableLot or animals} is
	 *                              {@code null}
	 */
	public TileAnimalPair playTour(GameLot availableLot, FauneBag animals) {
		Objects.requireNonNull(availableLot);
		Objects.requireNonNull(animals);
		System.out.println("Environement of " + name + " :");
		System.out.println(board);
		availableLot.checkNumberFaune(animals);
		System.out.println(name + ", Choose a tile to place :\n" + availableLot);
		var chosenPair = chooseLot(availableLot);
		tilePlacing(chosenPair.tile());

		if (!board.canPlaceAnimal(chosenPair.animal())) {
			System.out.println("Animal : " + chosenPair.animal() + " can not be placed");
			animals.addAnimal(chosenPair.animal());
			return chosenPair;
		}

		animalPlacing(chosenPair.animal());
		return chosenPair;
	}

	public TileAnimalPair playTourViewCarre(GameViewSquare gameViewsquare, ApplicationContext context, int lotStartX,
			int lotStartY, FauneBag animals) {
		Objects.requireNonNull(gameViewsquare);
		Objects.requireNonNull(context);
		context.renderFrame(graphics -> {
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
		});
		context.renderFrame(graphics -> gameViewsquare.render(graphics, name));
		var chosenPair = gameViewsquare.chooseLotClick(context, 1200, 100);
		var tilePlaced = false;
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (!tilePlaced) {
				tilePlaced = gameViewsquare.placeTileClick(event, chosenPair.tile(), name) == 1;
				context.renderFrame(graphics -> gameViewsquare.render(graphics, name));
			} else {
				if (!board.canPlaceAnimal(chosenPair.animal())) {
					context.renderFrame(graphics -> gameViewsquare.animalPlacementError(graphics, chosenPair.animal()));
					animals.addAnimal(chosenPair.animal());
					return chosenPair;
				}
				boolean animalPlaced = gameViewsquare.placeAnimalClick(event, chosenPair.animal(), name) == 1;
				if (animalPlaced) {
					tilePlaced = false;
					context.renderFrame(graphics -> gameViewsquare.render(graphics, name));
					break;
				}
			}
		}
		return chosenPair;
	}

	public void playTourViewHex(GameViewHex gameView, ApplicationContext context, int lotStartX, int lotStartY,
			Tiles tiles, FauneBag animals) {
		Objects.requireNonNull(gameView);
		Objects.requireNonNull(context);
		Objects.requireNonNull(tiles);
		Objects.requireNonNull(animals);
		context.renderFrame(graphics -> {
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
		});
		context.renderFrame(graphics -> gameView.render(graphics, name));
		TileAnimalPair selectedPair = gameView.finalLotChoice(context, 1700, 100, 60, 40, name, tiles, animals);
		context.renderFrame(graphics -> gameView.renderLot(graphics, selectedPair));
		double rotationAngle = gameView.getRotation(context, selectedPair);
		System.out.println("L'utilisateur a confirmé avec un angle de rotation : " + rotationAngle + "°");
		var tilePlaced = false;
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (!tilePlaced) {
				tilePlaced = gameView.placeHexTileClick(event, selectedPair.tile(), name, 100, 100, 60) == 1;
				context.renderFrame(graphics -> gameView.render(graphics, name));
			} else {
				if (!board.canPlaceAnimal(selectedPair.animal())) {
					context.renderFrame(graphics -> gameView.animalPlacementError(graphics, selectedPair.animal()));
					animals.addAnimal(selectedPair.animal());
					break;
				}
				boolean animalPlaced = gameView.placeAnimalClick(event, selectedPair.animal(), name, 100, 100, 60) == 1;
				if (animalPlaced) {
					tilePlaced = false;
					context.renderFrame(graphics -> gameView.render(graphics, name));
					break;
				}
			}
		}
	}

	/**
	 * Saves the player's achievements to a file located in the "ressources/success" directory.
	 * The file includes the player's total score, nature tokens, scores by habitat and animal,
	 * and the list of achieved successes.
	 *
	 * @param animalScores   a map containing the scores of animals
	 * @param habitatScores  a map containing the scores of habitats
	 * @param totalScore     the player's total score
	 * @throws NullPointerException if {@code animalScores} or {@code habitatScores} is {@code null}
	 */
	public void saveAchievements(Map<Animal, Integer> animalScores, Map<Habitat, Integer> habitatScores, int totalScore) {
		Objects.requireNonNull(animalScores);
		Objects.requireNonNull(habitatScores);

		var allSuccesses = successManager.getAllSuccesses();
		var achievedSuccesses = checkAchievements(animalScores, habitatScores, totalScore);
		var filePath = prepareFilePath();

		if (filePath == null) {
			return; 
		}

		try (var writer = Files.newBufferedWriter(filePath)) {
			writeHeader(writer, totalScore);
			writeScores(writer, "Scores by Habitat:", habitatScores);
			writeScores(writer, "Scores by Animal:", animalScores);
			writeAchievements(writer, achievedSuccesses, allSuccesses);

			System.out.println("Achievements saved to " + filePath);
		} catch (IOException e) {
			System.err.println("Failed to save achievements: " + e.getMessage());
		}
	}

	/**
	 * Prepares the file path for saving the player's achievements.
	 * Ensures the "ressources/success" directory exists, and resolves the path for the player's file.
	 *
	 * @return the {@link Path} to the player's achievements file
	 *         or {@code null} if directory creation fails
	 */
	private Path prepareFilePath() {
		var directoryPath = Path.of("ressources", "success");
		var filePath = directoryPath.resolve("Player_" + name + "_Achievements.txt");

		try {
			Files.createDirectories(directoryPath);
			return filePath;
		} catch (IOException e) {
			System.err.println("Failed to create directory: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Writes the list of achieved successes to the file.
	 *
	 * @param writer          the {@link BufferedWriter} used to write the achievements
	 * @param achievedSuccesses the list of IDs of successes achieved by the player
	 * @param allSuccesses    a map of all success IDs and their descriptions
	 * @throws IOException if an I/O error occurs while writing
	 */
	private void writeAchievements(BufferedWriter writer, List<Integer> achievedSuccesses,
			Map<Integer, String> allSuccesses) throws IOException {
		writer.write("Achieved Successes:");
		writer.newLine();
		for (int successId : achievedSuccesses) {
			writer.write(successId + " - " + allSuccesses.get(successId));
			writer.newLine();
		}
	}

	/**
	 * Writes scores for habitats or animals to the file under the given header.
	 *
	 * @param writer  the {@link BufferedWriter} used to write the scores
	 * @param header  the title to write above the scores (e.g., "Scores by Habitat:")
	 * @param scores  a map of items (habitats or animals) to their respective scores
	 * @param <T>     the type of the keys in the map (e.g., {@link Habitat} or {@link Animal})
	 * @throws IOException if an I/O error occurs while writing
	 */
	private <T> void writeScores(BufferedWriter writer, String header, Map<T, Integer> scores) throws IOException {
		writer.write(header);
		writer.newLine();
		for (var entry : scores.entrySet()) {
			writer.write("  " + entry.getKey() + ": " + entry.getValue() + " pts");
			writer.newLine();
		}
	}

	/**
	 * Writes the player's basic information, including name, total score, and nature tokens, to the file.
	 *
	 * @param writer      the {@link BufferedWriter} used to write the header
	 * @param totalScore  the player's total score
	 * @throws IOException if an I/O error occurs while writing
	 */
	private void writeHeader(BufferedWriter writer, int totalScore) throws IOException {
		writer.write("Player: " + name);
		writer.newLine();
		writer.write("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		writer.newLine();
		writer.write("Total Score: " + totalScore);
		writer.newLine();
		writer.write("Nature Tokens: " + natureTokens);
		writer.newLine();
	}

	/**
	 * Checks and identifies the achievements the player has earned based on scores and conditions.
	 *
	 * @param animalScores  a map of animal scores
	 * @param habitatScores a map of habitat scores
	 * @param totalScore    the player's total score
	 * @return a list of IDs corresponding to the achieved successes
	 */
	private List<Integer> checkAchievements(Map<Animal, Integer> animalScores, Map<Habitat, Integer> habitatScores,
			int totalScore) {
		List<Integer> achieved = new ArrayList<>();

		checkTotalScoreAchievements(totalScore, achieved);

		checkNatureTokenAchievements(achieved);

		checkAnimalAchievements(animalScores, achieved);

		checkHabitatAchievements(habitatScores, achieved);

		return achieved;
	}

	/**
	 * Checks achievements related to the player's total score.
	 *
	 * @param totalScore the player's total score
	 * @param achieved   the list to which achieved success IDs will be added
	 */
	private void checkTotalScoreAchievements(int totalScore, List<Integer> achieved) {
		if (totalScore >= 80) achieved.add(1);
		if (totalScore >= 85) achieved.add(2);
		if (totalScore >= 90) achieved.add(3);
		if (totalScore >= 95) achieved.add(4);
		if (totalScore >= 100) achieved.add(5);
		if (totalScore >= 105) achieved.add(6);
		if (totalScore >= 110) achieved.add(7);
	}

	/**
	 * Checks achievements related to the player's nature tokens.
	 *
	 * @param achieved the list to which achieved success IDs will be added
	 */
	private void checkNatureTokenAchievements(List<Integer> achieved) {
		if (natureTokens == 0) achieved.add(8);
		if (natureTokens >= 5) achieved.add(22);
		if (natureTokens >= 10) achieved.add(23);
	}

	/**
	 * Checks achievements related to the player's animal scores.
	 *
	 * @param animalScores a map of animal scores
	 * @param achieved     the list to which achieved success IDs will be added
	 */
	private void checkAnimalAchievements(Map<Animal, Integer> animalScores, List<Integer> achieved) {
		if (animalScores.getOrDefault(Animal.BEAR, 0) == 0)
			achieved.add(9);
		if (animalScores.getOrDefault(Animal.ELK, 0) == 0)
			achieved.add(10);
		if (animalScores.getOrDefault(Animal.SALMON, 0) == 0)
			achieved.add(11);
		if (animalScores.getOrDefault(Animal.BUZZARD, 0) == 0)
			achieved.add(12);
		if (animalScores.getOrDefault(Animal.FOX, 0) == 0)
			achieved.add(13);
		if (animalScores.values().stream().anyMatch(score -> score > 10))
			achieved.add(14);
	}

	/**
	 * Checks achievements related to the player's habitat scores.
	 *
	 * @param habitatScores a map of habitat scores
	 * @param achieved      the list to which achieved success IDs will be added
	 */
	private void checkHabitatAchievements(Map<Habitat, Integer> habitatScores, List<Integer> achieved) {
		if (habitatScores.values().stream().allMatch(score -> score >= 5))
			achieved.add(16);
		if (habitatScores.values().stream().anyMatch(score -> score >= 12))
			achieved.add(17);

		long majorityCount = habitatScores.values().stream().filter(score -> score >= 7).count();
		if (majorityCount >= 3)
			achieved.add(15);
	}

}