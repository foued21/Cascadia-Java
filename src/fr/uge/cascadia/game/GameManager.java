package fr.uge.cascadia.game;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;

import fr.uge.cascadia.score.MajorityBonus;
import fr.uge.cascadia.score.SuccessManager;
import fr.uge.cascadia.structure.FauneBag;
import fr.uge.cascadia.structure.GameLot;
import fr.uge.cascadia.structure.Tiles;
import fr.uge.cascadia.view.GameViewHex;
import fr.uge.cascadia.view.GameViewSquare;
import fr.uge.cascadia.view.ImageLoader;

/**
 * Manages the Cascadia game, including initialization, turns, and score
 * calculation.
 */
public class GameManager {
	private final Tiles tiles = new Tiles();
	private final FauneBag animals = new FauneBag();
	private final GameLot availableLot = new GameLot();
	private final Map<String, Player> players = new HashMap<>();

	/**
	 * Constructs a new {@code GameManager} instance, initializing the game
	 * environment, players, and the available tiles and lots.
	 *
	 * <p>
	 * This constructor:
	 * <ul>
	 * <li>Initializes two players with random starting tiles.</li>
	 * <li>Sets up the initial game tiles and available tile-animal pairs.</li>
	 * </ul>
	 */
	public GameManager(int nbPlayers, List<String> names, int mode) {
		Objects.requireNonNull(names);
		tiles.gameTiles(nbPlayers, mode == 3);
		var successManager = new SuccessManager();
		try {
			successManager.loadSuccesses();
		} catch (IOException e) {
			e.printStackTrace();
		}
		var firstTiles = (mode == 3) ? tiles.initHexFirstTiles() : tiles.initFirstTiles();
		for (int i = 1; i <= nbPlayers; i++) {
			var board = (mode == 3) ? new Environnement(5, 5, true) : new Environnement(5, 5, false);
			board.putFirstTile(tiles.randomFirstTile(firstTiles));
			players.put(names.get(i - 1), new Player(names.get(i - 1), board, successManager));
		}
		availableLot.initAvailablePair(tiles, animals);
	}

	/**
	 * Retrieves the player associated with the specified name.
	 *
	 * @param name the name of the player
	 * @return the {@link Player} object associated with the given name
	 * @throws NullPointerException if the name is {@code null}
	 */
	public Player getPlayer(String name) {
		Objects.requireNonNull(name);
		return players.get(name);
	}

	/**
	 * Retrieves the current available tile-animal pairs (lots) for the game.
	 *
	 * @return the {@link GameLot} containing the available tile-animal pairs
	 */
	public GameLot getAvailableLot() {
		return availableLot;
	}

	/**
	 * Retrieves the bag of animals used in the game.
	 *
	 * @return the {@link FauneBag} containing the animals
	 */
	public FauneBag getAnimals() {
		return animals;
	}

	private static int nb_tours(int nb_players) {
		return nb_players * 20;
	}

	/**
	 * Runs the main game loop for a specified number of turns.
	 *
	 * <p>
	 * This method alternates turns between the two players, allowing each player
	 * to:
	 * <ul>
	 * <li>Select and place a tile from the available lots.</li>
	 * <li>Place an associated animal if possible.</li>
	 * </ul>
	 * After all turns are completed, the final scores for both players are
	 * calculated and displayed.
	 *
	 * @param numberOfTurns the number of turns to play
	 * @throws IllegalArgumentException if {@code numberOfTurns} is negative
	 */
	private void playGame(int numberOfTurns) {

		var playerList = new ArrayList<>(players.values());
		for (int turn = 0; turn < numberOfTurns; turn++) {
			var currentPlayer = playerList.get(turn % playerList.size());
			System.out.println("Player: " + currentPlayer.getName());

			var selectedLot = currentPlayer.playTour(availableLot, animals);
			availableLot.replaceSelectedLot(selectedLot, tiles, animals);

			System.out.println("Environment of " + currentPlayer.getName() + ":");
			System.out.println(currentPlayer.getBoard());
		}

	}

	/**
	 * Executes the Zen mode of the game with square tiles.
	 *
	 * @param numberOfTurns the number of turns in the game
	 * @param gameView      the {@link GameView} instance to render the game
	 * @param context       the {@link ApplicationContext} for rendering and event
	 *                      handling
	 * @param lotStartX     the X-coordinate for the starting position of lots
	 * @param lotStartY     the Y-coordinate for the starting position of lots
	 */
	private void playGameZenCarre(int numberOfTurns, GameViewSquare gameViewsquare, ApplicationContext context,
			int lotStartX, int lotStartY) {
		var playerList = new ArrayList<>(players.values());
		for (int turn = 0; turn < numberOfTurns; turn++) {
			var currentPlayer = playerList.get(turn % playerList.size());
			var chosenPair = currentPlayer.playTourViewCarre(gameViewsquare, context, lotStartX, lotStartY, animals);
			availableLot.replaceSelectedLot(chosenPair, tiles, animals);
			context.renderFrame(graphics -> gameViewsquare.render(graphics, currentPlayer.getName()));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Executes the Zen mode of the game with hexagonal tiles.
	 *
	 * @param numberOfTurns the number of turns in the game
	 * @param gameView      the {@link GameViewHex} instance to render the game
	 * @param context       the {@link ApplicationContext} for rendering and event
	 *                      handling
	 * @param lotStartX     the X-coordinate for the starting position of lots
	 * @param lotStartY     the Y-coordinate for the starting position of lots
	 */
	private void playGameZenHex(int numberOfTurns, GameViewHex gameView, ApplicationContext context, int lotStartX,
			int lotStartY) {
		var playerList = new ArrayList<>(players.values());
		for (int turn = 0; turn < numberOfTurns; turn++) {
			var currentPlayer = playerList.get(turn % playerList.size());
			currentPlayer.playTourViewHex(gameView, context, lotStartX, lotStartY, tiles, animals);
			context.renderFrame(graphics -> gameView.render(graphics, currentPlayer.getName()));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("fin de la partie");
	}

	/**
	 * Calculates and displays scores in the terminal for all players.
	 *
	 * @param variant the game variant (e.g., normal, challenge mode)
	 * @param card    the name or identifier of the scoring card used
	 */
	private void scoreCalculaterTerminal(int variant, String card) {
		var majorityBonus = new MajorityBonus(players.values(), players.size());
		var bonuses = majorityBonus.getBonuses();
		players.values().forEach(player -> {
			var animalScores = player.getScoreCalculator().calculateAnimalsScore(variant, card, animals);
			var tileScores = player.getScoreCalculator().calculateTilesScore();
			var playerBonuses = bonuses.getOrDefault(player, Map.of());

			System.out.println("------ Scores for " + player.getName() + " ------");
			System.out.println("------ Animal Scores ------");
			animalScores.forEach((animal, score) -> System.out.println(animal + " Score: " + score));

			System.out.println("------ Habitat Scores ------");
			tileScores.forEach((habitat, score) -> {
				int bonus = playerBonuses.getOrDefault(habitat, 0);
				System.out.println(habitat + " Score: " + score + (bonus > 0 ? " (Bonus: +" + bonus + " points)" : ""));
			});

			int totalScore = player.getScoreCalculator().calculateTotalScore(animalScores, tileScores, playerBonuses, 0);
			System.out.println("Total Score: " + totalScore);
			System.out.println("-----------------------------------");
			player.saveAchievements(animalScores, tileScores, totalScore);
		});
	}

	/**
	 * Calculates and displays scores for all players in a graphical view.
	 *
	 * @param gameView  the {@link GameView} instance to render scores
	 * @param variant   the game variant (e.g., normal, challenge mode)
	 * @param card      the name or identifier of the scoring card used
	 * @param context   the {@link ApplicationContext} for rendering and event
	 *                  handling
	 * @param nbPlayers the number of players in the game
	 */
	private void scoreCalculaterSquareView(GameViewSquare gameViewsquare, int variant, String card,
			ApplicationContext context, int nbPlayers) {
		var majorityBonus = new MajorityBonus(players.values(), nbPlayers);
		var bonuses = majorityBonus.getBonuses();
		players.values().forEach(player -> {
			var animalScores = player.getScoreCalculator().calculateAnimalsScore(variant, card, animals);
			var tileScores = player.getScoreCalculator().calculateTilesScore();
			var playerBonuses = bonuses.getOrDefault(player, Map.of());
			int totalScore = player.getScoreCalculator().calculateTotalScore(animalScores, tileScores, playerBonuses, player.getNatureTokens());
      player.saveAchievements(animalScores, tileScores, totalScore);
			context.renderFrame(graphics -> {
				graphics.setColor(Color.LIGHT_GRAY);
				graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
			});
			context.renderFrame(graphics -> {
				gameViewsquare.showCombinedScoreZen(graphics, animalScores, tileScores, playerBonuses, player.getName());
			});
			GameViewSquare.waitForClick(context);
		});
	}

	/**
	 * Calculates and displays scores for all players in a graphical view for
	 * hexagonal tiles.
	 *
	 * @param gameView  the {@link GameViewHex} instance to render scores
	 * @param variant   the game variant (e.g., normal, challenge mode)
	 * @param card      the name or identifier of the scoring card used
	 * @param context   the {@link ApplicationContext} for rendering and event
	 *                  handling
	 * @param nbPlayers the number of players in the game
	 */
	private void scoreCalculaterHexView(GameViewHex gameView, int variant, String card, ApplicationContext context,
			int nbPlayers) {
		var majorityBonus = new MajorityBonus(players.values(), nbPlayers);
		var bonuses = majorityBonus.getBonuses();
		players.values().forEach(player -> {
			var animalScores = player.getScoreCalculator().calculateAnimalsScore(variant, card, animals);
			var tileScores = player.getScoreCalculator().calculateTilesScore();
			var playerBonuses = bonuses.getOrDefault(player, Map.of());
			var natureTokens = player.getNatureTokens();
			int totalScore = player.getScoreCalculator().calculateTotalScore(animalScores, tileScores, playerBonuses, natureTokens);
      player.saveAchievements(animalScores, tileScores, totalScore);
			context.renderFrame(graphics -> {
				graphics.setColor(Color.LIGHT_GRAY);
				graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
			});
			context.renderFrame(graphics -> {
				gameView.showCombinedScoreZen(graphics, animalScores, tileScores, playerBonuses, natureTokens,
						player.getName());
			});

			GameViewSquare.waitForClick(context);
		});
	}


	/**
	 * Starts the game based on the selected mode and variant.
	 *
	 * <p>
	 * The game supports three modes:
	 * <ul>
	 * <li>Mode 1: Terminal-based gameplay</li>
	 * <li>Mode 2: Graphical gameplay with square tiles</li>
	 * <li>Mode 3: Graphical gameplay with hexagonal tiles</li>
	 * </ul>
	 *
	 * @throws InterruptedException if the thread is interrupted during sleep
	 */
	public static void startGame(ImageLoader imageLoader) {
		Objects.requireNonNull(imageLoader);
		var mode = GameChoices.gameModeChoice();
		var variant = GameChoices.variantChoice();
		var card = variant == 3 ? GameChoices.cardChoice() : "0";
		var nbPlayers = GameChoices.modeChoice()==1 ? 1 : GameChoices.numberPlayersChoice();
		var names = GameChoices.playerNames(nbPlayers);
		var game = new GameManager(nbPlayers, names, mode);
		if (mode == 1) {
			game.playGame(nb_tours(nbPlayers));
			game.scoreCalculaterTerminal(variant, card);
		} else if (mode == 2) {
			Application.run(Color.LIGHT_GRAY, context -> {
				var gameView = new GameViewSquare(game, imageLoader);
				game.playGameZenCarre(nb_tours(nbPlayers), gameView, context, 1700, 100);
				game.scoreCalculaterSquareView(gameView, variant, card, context, nbPlayers);
			});
		} else if (mode == 3) {
			Application.run(Color.LIGHT_GRAY, context -> {
				var gameView = new GameViewHex(game, imageLoader);
				game.playGameZenHex(10, gameView, context, 100, 100);
				game.scoreCalculaterHexView(gameView, variant, card, context, nbPlayers);
			});
		}
	}
}
