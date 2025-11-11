package fr.uge.cascadia.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.PointerEvent;

import fr.uge.cascadia.game.Environnement;
import fr.uge.cascadia.game.GameManager;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.Habitat;
import fr.uge.cascadia.structure.Tile;
import fr.uge.cascadia.structure.TileAnimalPair;

/**
 * Provides methods to render and interact with the graphical interface of the
 * game Cascadia.
 * 
 * <p>
 * The {@code GameView} class is responsible for:
 * <ul>
 * <li>Rendering the player's board and available tiles/animals</li>
 * <li>Handling user input for selecting and placing tiles or animals</li>
 * <li>Displaying scores and other game information</li>
 * </ul>
 */

public class GameViewSquare {
	private final GameManager gameManager;
	private final ImageLoader imageLoader;

	/**
	 * Constructs a {@code GameView} with the specified {@link GameManager}.
	 *
	 * @param gameManager the game manager that manages the game state
	 * @param imageLoader allows to load pictures
	 */
	public GameViewSquare(GameManager gameManager, ImageLoader imageLoader) {
		this.gameManager = Objects.requireNonNull(gameManager);
		this.imageLoader = Objects.requireNonNull(imageLoader);

	}

	/**
	 * Renders the game view, including the player's board and available lots.
	 *
	 * @param graphics the {@link Graphics2D} instance to draw on
	 * @param name     the name of the player whose board is being rendered
	 * @throws NullPointerException if {@code graphics} or {@code name} is
	 *                              {@code null}
	 */
	public void render(Graphics2D graphics, String name) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(name);
		graphics.setColor(Color.BLACK);
		graphics.setFont(graphics.getFont().deriveFont(30f));
		graphics.drawString("Player: " + name, 100, 80);
		drawPlayerBoard(graphics, gameManager.getPlayer(name).getBoard(), 100, 100);
		showLot(graphics, 1200, 100);
	}

	/**
	 * Draws the player's board.
	 *
	 * @param graphics the {@link Graphics2D} instance to draw on
	 * @param board    the player's board to render
	 * @param startX   the x-coordinate of the top-left corner
	 * @param startY   the y-coordinate of the top-left corner
	 */
	private void drawPlayerBoard(Graphics2D graphics, Environnement board, int startX, int startY) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(board);
		int tileSize = 100;
		for (int row = 0; row < board.getRowCount(); row++) {
			for (int col = 0; col < board.getColCount(); col++) {
				var lot = board.getTile(row, col);
				int x = startX + col * tileSize;
				int y = startY + row * tileSize;
				if (lot != null) {
					if (lot.isTile()) {
						drawTile(graphics, lot.asTile(), x, y, tileSize);
					} else if (lot.isTileAnimalPair()) {
						drawTileAnimalPair(graphics, lot.asTileAnimalPair(), x, y, tileSize);
					}
				} else {
					drawEmptyTile(graphics, x, y, tileSize);
				}
			}
		}
	}

	/**
	 * Draws a tile.
	 *
	 * @param graphics the {@link Graphics2D} instance to draw on
	 * @param tile     the tile to render
	 * @param x        the x-coordinate of the tile
	 * @param y        the y-coordinate of the tile
	 * @param size     the size of the tile
	 */
	private void drawTile(Graphics2D graphics, Tile tile, int x, int y, int size) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(tile);
		var habitatName = tile.getHabitats()[0].toString().toLowerCase();
		var animalName1 = tile.getAnimals()[0].toString().toLowerCase();
		var animalName2 = tile.getAnimals()[1].toString().toLowerCase();
		BufferedImage tileImage = imageLoader.getImage(habitatName + ".jpg");
		BufferedImage animalImage1 = imageLoader.getImage(animalName1 + ".png");
		BufferedImage animalImage2 = imageLoader.getImage(animalName2 + ".png");
		graphics.drawImage(tileImage, x, y, size, size, null);
		int animalSize = size / 3;
		int offset = size / 8;

		int animal1X = x + offset;
		int animal1Y = y + offset;

		int animal2X = x + size - animalSize - offset;
		int animal2Y = y + size - animalSize - offset;
		graphics.drawImage(animalImage1, animal1X, animal1Y, animalSize, animalSize, null);
		graphics.drawImage(animalImage2, animal2X, animal2Y, animalSize, animalSize, null);
	}

	/**
	 * Draws a {@link TileAnimalPair}.
	 *
	 * @param graphics the {@link Graphics2D} instance to draw on
	 * @param pair     the tile-animal pair to render
	 * @param x        the x-coordinate of the pair
	 * @param y        the y-coordinate of the pair
	 * @param size     the size of the tile
	 */
	private void drawTileAnimalPair(Graphics2D graphics, TileAnimalPair pair, int x, int y, int size) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(pair);
		var tileName = pair.tile().getHabitats()[0].toString().toLowerCase();
		var animalName = pair.animal().toString().toLowerCase();
		var tileImage = imageLoader.getImage(tileName + ".jpg");
		var animalImage = imageLoader.getImage(animalName + ".png");

		graphics.drawImage(tileImage, x, y, size, size, null);
		graphics.drawImage(animalImage, x + size / 4, y + size / 4, size / 2, size / 2, null);
	}

	/**
	 * Draws an empty tile.
	 *
	 * @param graphics the {@link Graphics2D} instance to draw on
	 * @param x        the x-coordinate of the tile
	 * @param y        the y-coordinate of the tile
	 * @param size     the size of the tile
	 */
	private void drawEmptyTile(Graphics2D graphics, int x, int y, int size) {
		graphics.drawRect(x, y, size, size);
	}

	/**
	 * Displays an error message when an animal cannot be placed on the board.
	 *
	 * @param graphics the {@link Graphics2D} instance to draw the message
	 * @param animal   the {@link Animal} that cannot be placed
	 * @throws NullPointerException if {@code graphics} or {@code animal} is
	 *                              {@code null}
	 */
	public void animalPlacementError(Graphics2D graphics, Animal animal) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(animal);
		graphics.setFont(graphics.getFont().deriveFont(30f)); // Taille de police de 20
		graphics.drawString("Animal : " + animal + " can not be placed", 800, 600);

	}

	/**
	 * Renders the list of available lots on the screen.
	 *
	 * @param graphics the {@link Graphics2D} instance to draw the lots
	 * @param startX   the x-coordinate where the rendering starts
	 * @param startY   the y-coordinate where the rendering starts
	 * @throws NullPointerException if {@code graphics} is {@code null}
	 */
	public void showLot(Graphics2D graphics, int startX, int startY) {
		Objects.requireNonNull(graphics);
		int lotSize = 100, padding = 20;
		var lots = gameManager.getAvailableLot();
		for (int i = 0; i < lots.size(); i++) {
			var pair = lots.get(i);
			int x = startX;
			int y = startY + i * (lotSize + padding);
			drawTile(graphics, pair.tile(), x, y, lotSize);

			BufferedImage animalImage = imageLoader.getImage(pair.animal().toString().toLowerCase() + ".png");
			if (animalImage != null) {
				graphics.drawImage(animalImage, x + lotSize + padding, y + lotSize - (2 * padding), lotSize / 2, lotSize / 2,
						null);
			} else {
				System.err.println("Image for animal not found.");
			}
			lots.checkNumberFaune(gameManager.getAnimals());
		}
	}

	/**
	 * Waits for the user to click on an available lot and returns the selected
	 * {@link TileAnimalPair}.
	 *
	 * @param context the {@link ApplicationContext} to handle events
	 * @param startX  the x-coordinate where the lots start
	 * @param startY  the y-coordinate where the lots start
	 * @return the selected {@link TileAnimalPair}
	 * @throws NullPointerException if {@code context} is {@code null}
	 */
	public TileAnimalPair chooseLotClick(ApplicationContext context, int startX, int startY) {
		Objects.requireNonNull(context);
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event != null) {
				switch (event) {
				case PointerEvent e:
					if (e.action() == PointerEvent.Action.POINTER_DOWN) {
						var location = e.location();
						int lotSize = 100, padding = 20;
						var xClick = (int) location.x();
						var yClick = (int) location.y();
						var lotIndex = (yClick - startY) / (lotSize + padding);
						if (lotIndex >= 0 && lotIndex < gameManager.getAvailableLot().size() && xClick >= startX) {
							return gameManager.getAvailableLot().get(lotIndex); // Retourne le lot sélectionné
						}
					}
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * Handles the placement of a tile on the board based on user clicks.
	 *
	 * @param event the {@link Event} to handle user input
	 * @param tile  the {@link Tile} to be placed
	 * @param name  the name of the player placing the tile
	 * @return 1 if the tile is placed successfully, 0 otherwise
	 * @throws NullPointerException if {@code tile} or {@code name} is {@code null}
	 */
	public int placeTileClick(Event event, Tile tile, String name) {
		Objects.requireNonNull(tile);
		Objects.requireNonNull(name);
		if (event != null) {
			switch (event) {
			case PointerEvent e:
				if (e.action() == PointerEvent.Action.POINTER_DOWN) {
					var location = e.location();

					int row = (int) (location.y() / 100);
					int col = (int) (location.x() / 100);

					return gameManager.getPlayer(name).getBoard().placeTile(tile, row, col);
				}
			default:
				return 0;
			}
		}
		return 0;
	}

	/**
	 * Handles the placement of an animal on the board based on user clicks.
	 *
	 * @param event  the {@link Event} to handle user input
	 * @param animal the {@link Animal} to be placed
	 * @param name   the name of the player placing the animal
	 * @return 1 if the animal is placed successfully, 0 otherwise
	 * @throws NullPointerException if {@code animal} or {@code name} is
	 *                              {@code null}
	 */
	public int placeAnimalClick(Event event, Animal animal, String name) {
		Objects.requireNonNull(animal);
		Objects.requireNonNull(name);
		if (event != null) {
			switch (event) {
			case PointerEvent e:
				if (e.action() == PointerEvent.Action.POINTER_DOWN) {
					var location = e.location();

					int row = (int) (location.y() / 100);
					int col = (int) (location.x() / 100);

					return gameManager.getPlayer(name).getBoard().placeAnimal(animal, row, col);
				}
			default:
				return 0;
			}
		}
		return 0;
	}

	/**
	 * Waits for the user to click anywhere in the graphical interface.
	 *
	 * @param context the {@link ApplicationContext} to handle events
	 * @throws NullPointerException if {@code context} is {@code null}
	 */
	public static void waitForClick(ApplicationContext context) {
		boolean clicked = false;
		while (!clicked) {
			var event = context.pollOrWaitEvent(10);
			if (event != null) {
				switch (event) {
				case PointerEvent e:
					if (e.action() == PointerEvent.Action.POINTER_DOWN) {
						clicked = true;
					}
				default:
					break;
				}
			}
		}
	}

	/**
	 * Displays the scores related to animals for a specific player on the graphical
	 * interface.
	 *
	 * @param graphics the {@link Graphics2D} instance used for rendering
	 * @param results  a map containing the animal scores, where the key is the
	 *                 {@link Animal} and the value is its score
	 * @param name     the name of the player whose scores are being displayed
	 * @throws NullPointerException if {@code graphics}, {@code results}, or
	 *                              {@code name} is {@code null}
	 */
	private void showAnimalsScore(Graphics2D graphics, Map<Animal, Integer> results, String name) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(results);
		Objects.requireNonNull(name);
		graphics.setColor(Color.BLACK);
		graphics.setFont(graphics.getFont().deriveFont(25f));

		int startX = 1500, startY = 100;
		int yOffset = startY;
		graphics.drawString(name + " score:", startX, yOffset);
		yOffset += 25;
		for (var elem : results.entrySet()) {
			graphics.drawString(elem.getKey() + " Score: " + elem.getValue(), startX, yOffset);
			yOffset += 25;
		}
	}

	/**
	 * Displays the scores related to tiles and bonuses for a specific player on the
	 * graphical interface.
	 *
	 * @param graphics   the {@link Graphics2D} instance used for rendering
	 * @param tileScores a map containing the scores for each {@link Habitat}, where
	 *                   the key is the habitat and the value is its score
	 * @param bonuses    a map containing the bonus scores for each {@link Habitat},
	 *                   where the key is the habitat and the value is its bonus
	 * @param name       the name of the player whose scores are being displayed
	 * @throws NullPointerException if {@code graphics}, {@code tileScores},
	 *                              {@code bonuses}, or {@code name} is {@code null}
	 */
	private void showTilesScore(Graphics2D graphics, Map<Habitat, Integer> tileScores, Map<Habitat, Integer> bonuses,
			String name) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(tileScores);
		Objects.requireNonNull(bonuses);
		Objects.requireNonNull(name);

		graphics.setColor(Color.BLACK);
		graphics.setFont(graphics.getFont().deriveFont(25f));

		int startX = 900;
		int startY = 260;

		int yOffset = startY;
		graphics.drawString("------ TILES SCORE ------", startX, yOffset);
		yOffset += 25;

		for (var habitat : Habitat.allHabitats()) {
			int score = tileScores.getOrDefault(habitat, 0);
			int bonus = bonuses.getOrDefault(habitat, 0);
			String bonusText = bonus > 0 ? "Bonus: +" + bonus + " points" : "No Bonus";
			graphics.drawString(habitat + " Score: " + score + " (" + bonusText + ")", startX, yOffset);
			yOffset += 25;
		}
	}

	/**
	 * Displays the combined scores (animal scores, tile scores, and bonuses) for a
	 * specific player on the graphical interface.
	 *
	 * @param graphics     the {@link Graphics2D} instance used for rendering
	 * @param animalScores a map containing the animal scores, where the key is the
	 *                     {@link Animal} and the value is its score
	 * @param tileScores   a map containing the scores for each {@link Habitat},
	 *                     where the key is the habitat and the value is its score
	 * @param bonuses      a map containing the bonus scores for each
	 *                     {@link Habitat}, where the key is the habitat and the
	 *                     value is its bonus
	 * @param name         the name of the player whose scores are being displayed
	 * @throws NullPointerException if {@code graphics}, {@code animalScores},
	 *                              {@code tileScores}, {@code bonuses}, or
	 *                              {@code name} is {@code null}
	 */
	public void showCombinedScoreZen(Graphics2D graphics, Map<Animal, Integer> animalScores,
			Map<Habitat, Integer> tileScores, Map<Habitat, Integer> bonuses, String name) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(animalScores);
		Objects.requireNonNull(tileScores);
		Objects.requireNonNull(bonuses);
		Objects.requireNonNull(name);

		drawPlayerBoard(graphics, gameManager.getPlayer(name).getBoard(), 100, 100);
		showAnimalsScore(graphics, animalScores, name);
		showTilesScore(graphics, tileScores, bonuses, name);
		graphics.drawString(
				"total : "
						+ gameManager.getPlayer(name).getScoreCalculator().calculateTotalScore(animalScores, tileScores, bonuses, 0),
				1500, 450);
	}
}