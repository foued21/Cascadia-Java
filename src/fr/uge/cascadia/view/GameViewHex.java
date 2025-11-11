package fr.uge.cascadia.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.PointerEvent;

import fr.uge.cascadia.game.Environnement;
import fr.uge.cascadia.game.GameManager;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.FauneBag;
import fr.uge.cascadia.structure.Habitat;
import fr.uge.cascadia.structure.Lot;
import fr.uge.cascadia.structure.Tile;
import fr.uge.cascadia.structure.TileAnimalPair;
import fr.uge.cascadia.structure.Tiles;

/**
 * Manages the graphical rendering and user interactions for the Cascadia game
 * with hexagonal tiles.
 */

public class GameViewHex {
	private final GameManager gameManager;
	private final ImageLoader imageLoader;

	/**
	 * Creates a new instance of {@code GameViewHex}.
	 *
	 * @param gameManager the game manager to manage game state and logic
	 * @param imageLoader the image loader for fetching graphical assets
	 * @throws NullPointerException if {@code gameManager} or {@code imageLoader} is
	 *                              null
	 */
	public GameViewHex(GameManager gameManager, ImageLoader imageLoader) {

		this.gameManager = Objects.requireNonNull(gameManager);
		this.imageLoader = Objects.requireNonNull(imageLoader);
	}

	/**
	 * Renders the player's game interface, including their board and available
	 * actions.
	 *
	 * @param graphics the graphics context used for rendering
	 * @param name     the name of the player whose interface is being rendered
	 * @throws NullPointerException if {@code graphics} or {@code name} is null
	 */
	public void render(Graphics2D graphics, String name) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(name);
		graphics.setColor(Color.BLACK);
		graphics.setFont(graphics.getFont().deriveFont(30f));
		graphics.drawString("Player: " + name, 50, 30);
		graphics.drawString("nature tokens : " + gameManager.getPlayer(name).getNatureTokens(), 300, 30);
		drawNTbutton(graphics, 600, 5, 200, 30);
		drawRotationButton(graphics, 870, 900, 100, 50);
		drawConfirmButton(graphics, 1000, 900, 100, 50);
		drawHexPlayerBoard(graphics, gameManager.getPlayer(name).getBoard(), 100, 100);
		showHexLot(graphics, 1700, 100);
	}

	/**
	 * Renders the selected tile-animal pair in the interface.
	 *
	 * @param graphics     the graphics context used for rendering
	 * @param selectedPair the tile-animal pair selected by the player
	 * @throws NullPointerException if {@code graphics} or {@code selectedPair} is
	 *                              null
	 */
	public void renderLot(Graphics2D graphics, TileAnimalPair selectedPair) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(selectedPair);
		showSelectedLot(graphics, selectedPair, 870, 800, 60);
	}

	/**
	 * Draws the player's game board with the current state of tiles and animals.
	 *
	 * @param graphics the graphics context used for rendering
	 * @param board    the player's game board
	 * @param startX   the X-coordinate for the board's starting position
	 * @param startY   the Y-coordinate for the board's starting position
	 * @throws NullPointerException if {@code graphics} or {@code board} is null
	 */
	private void drawHexPlayerBoard(Graphics2D graphics, Environnement board, int startX, int startY) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(board);
		var tileSize = 60;
		double dx = tileSize * Math.sqrt(3), dy = tileSize * 1.5;
		for (int row = 0; row < board.getRowCount(); row++) {
			for (int col = 0; col < board.getColCount(); col++) {
				double offset = (dx / 2) * (row % 2), centerX = startX + col * dx + offset, centerY = startY + row * dy;

				var lot = board.getTile(row, col);
				if (lot != null) {
					if (lot.isTile()) {
						drawHexTile(graphics, lot.asTile(), centerX, centerY, tileSize,
								Math.toRadians(lot.asTile().getCurrentRotation() * 60));
					} else if (lot.isTileAnimalPair()) {
						drawHexTileAnimalPair(graphics, lot.asTileAnimalPair(), centerX, centerY, tileSize,
								Math.toRadians(lot.asTileAnimalPair().tile().getCurrentRotation() * 60));
					}
				} else {
					drawEmptyHexTile(graphics, centerX, centerY, tileSize);
				}
			}
		}
	}

	/**
	 * Draws an empty hexagonal tile on the graphics context.
	 *
	 * @param graphics the graphics context used for rendering
	 * @param centerX  the X-coordinate of the tile's center
	 * @param centerY  the Y-coordinate of the tile's center
	 * @param size     the size (radius) of the hexagon
	 */
	private void drawEmptyHexTile(Graphics2D graphics, double centerX, double centerY, int size) {
		Polygon hex = calculateHexagon(centerX, centerY, size);
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillPolygon(hex);
		graphics.setColor(Color.BLACK);
		graphics.drawPolygon(hex);
	}

	/**
	 * Draws a hexagonal tile on the graphics context, including its habitats and
	 * animals.
	 *
	 * @param graphics the graphics context used for rendering
	 * @param tile     the tile to draw
	 * @param centerX  the X-coordinate of the tile's center
	 * @param centerY  the Y-coordinate of the tile's center
	 * @param size     the size (radius) of the hexagon
	 * @param angle    the rotation angle of the tile in radians
	 * @throws NullPointerException if {@code graphics} or {@code tile} is null
	 */
	private void drawHexTile(Graphics2D graphics, Tile tile, double centerX, double centerY, int size, double angle) {
		Objects.requireNonNull(tile);
		Objects.requireNonNull(graphics);

		drawEmptyHexTile(graphics, centerX, centerY, size);

		drawTileHabitats(graphics, tile, centerX, centerY, size, angle);

		drawTileAnimals(graphics, tile, centerX, centerY, size);
	}

	/**
	 * Rotates an image by a specified angle and draws it on the graphics context.
	 *
	 * @param image    the image to rotate
	 * @param angle    the rotation angle in radians
	 * @param graphics the graphics context used for rendering
	 * @param centerX  the X-coordinate for the center of rotation
	 * @param centerY  the Y-coordinate for the center of rotation
	 * @throws NullPointerException if {@code graphics} or {@code image} is null
	 */
	public static void rotateImage(BufferedImage image, double angle, Graphics2D graphics, int centerX, int centerY) {
		Objects.requireNonNull(graphics, "Graphics2D ne peut pas être null");
		Objects.requireNonNull(image, "Image ne peut pas être null");
		var transform = new AffineTransform();
		transform.translate(centerX - image.getWidth() / 2.0, centerY - image.getHeight() / 2.0);
		transform.rotate(angle, image.getWidth() / 2.0, image.getHeight() / 2.0);
		graphics.drawImage(image, transform, null);
	}

	/**
	 * Draws the habitats of a tile on the graphics context.
	 *
	 * @param graphics the graphics context used for rendering
	 * @param tile     the tile whose habitats will be drawn
	 * @param centerX  the X-coordinate of the tile's center
	 * @param centerY  the Y-coordinate of the tile's center
	 * @param size     the size (radius) of the hexagon
	 * @param angle    the rotation angle of the tile in radians
	 * @throws NullPointerException if {@code graphics} or {@code tile} is null
	 */
	private void drawTileHabitats(Graphics2D graphics, Tile tile, double centerX, double centerY, int size,
			double angle) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(tile);

		String habitatImageName = null;
		String habitatImageName2 = null;

		if (tile.getHabitats().length == 1) {
			habitatImageName = tile.getHabitats()[0].toString().toLowerCase() + ".png";
		} else if (tile.getHabitats().length == 2) {
			habitatImageName = tile.getHabitats()[0].toString().toLowerCase() + "_"
					+ tile.getHabitats()[1].toString().toLowerCase() + ".png";
			habitatImageName2 = tile.getHabitats()[1].toString().toLowerCase() + "_"
					+ tile.getHabitats()[0].toString().toLowerCase() + ".png";
		} else {
			return;
		}

		var habitatImage = imageLoader.getImage(habitatImageName);
		if (habitatImage == null && habitatImageName2 != null) {
			habitatImage = imageLoader.getImage(habitatImageName2);
		}

		if (habitatImage != null) {
			rotateImage(habitatImage, angle, graphics, (int) centerX, (int) centerY);
		}
	}

	/**
	 * Draws the animals on a tile, positioning them appropriately based on the
	 * number of animals.
	 *
	 * @param graphics the graphics context used for rendering
	 * @param tile     the tile containing the animals to draw
	 * @param centerX  the X-coordinate of the tile's center
	 * @param centerY  the Y-coordinate of the tile's center
	 * @param size     the size (radius) of the hexagon
	 * @throws NullPointerException if {@code graphics} or {@code tile} is null
	 */
	private void drawTileAnimals(Graphics2D graphics, Tile tile, double centerX, double centerY, int size) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(tile);

		int animalSize = size / 2;
		int offset = size / 8;

		if (tile.getAnimals().length == 1) {
			drawAnimal(graphics, tile.getAnimals()[0], centerX - animalSize / 2.0, centerY - animalSize / 2.0, animalSize);
		} else if (tile.getAnimals().length == 2) {
			drawAnimal(graphics, tile.getAnimals()[0], centerX - size / 2.0 + offset, centerY - size / 2.0 + offset,
					animalSize);
			drawAnimal(graphics, tile.getAnimals()[1], centerX + size / 2.0 - animalSize - offset,
					centerY + size / 2.0 - animalSize - offset, animalSize);
		} else if (tile.getAnimals().length == 3) {
			drawAnimal(graphics, tile.getAnimals()[0], centerX - size / 2.0 + offset + 2.0, centerY - size / 2.0 + 4.0,
					animalSize); // Haut
			drawAnimal(graphics, tile.getAnimals()[1], centerX - size / 2.0 + offset - 2.0, centerY - size / 2.0 + animalSize,
					animalSize);
			drawAnimal(graphics, tile.getAnimals()[2], centerX + size / 2.0 - offset - animalSize,
					centerY - size / 2.0 + animalSize, animalSize); // Bas droite
		}

	}

	/**
	 * Draws an animal at the specified position on the graphics context.
	 *
	 * @param graphics the graphics context used for rendering
	 * @param animal   the animal to draw
	 * @param x        the X-coordinate for the top-left corner of the animal
	 * @param y        the Y-coordinate for the top-left corner of the animal
	 * @param size     the size of the animal image
	 * @throws NullPointerException if {@code graphics} or {@code animal} is null
	 */
	private void drawAnimal(Graphics2D graphics, Animal animal, double x, double y, int size) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(animal);
		var animalName = animal.toString().toLowerCase();
		var animalImage = imageLoader.getImage(animalName + ".png");
		if (animalImage != null) {
			graphics.drawImage(animalImage, (int) x, (int) y, size, size, null);
		} else {
			System.err.println("Image for animal '" + animalName + "' not found.");
		}
	}

	/**
	 * Draws a hexagonal tile with an associated animal, applying the specified
	 * rotation.
	 *
	 * @param graphics the graphics context used for rendering
	 * @param pair     the tile-animal pair to draw
	 * @param centerX  the X-coordinate of the tile's center
	 * @param centerY  the Y-coordinate of the tile's center
	 * @param size     the size (radius) of the hexagon
	 * @param angle    the rotation angle of the tile in radians
	 * @throws NullPointerException if {@code graphics} or {@code pair} is null
	 */
	private void drawHexTileAnimalPair(Graphics2D graphics, TileAnimalPair pair, double centerX, double centerY, int size,
			double angle) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(pair);

		drawHexTile(graphics, pair.tile(), centerX, centerY, size, angle);
		drawAnimal(graphics, pair.animal(), (centerX - size / 2), (centerY - size / 2), size);
	}

	/**
	 * Calculates the vertices of a hexagon centered at the specified coordinates.
	 *
	 * @param centerX the X-coordinate of the hexagon's center
	 * @param centerY the Y-coordinate of the hexagon's center
	 * @param radius  the radius (distance from center to a vertex) of the hexagon
	 * @return a {@code Polygon} representing the hexagon
	 */
	private Polygon calculateHexagon(double centerX, double centerY, int radius) {
		int[] xPoints = new int[6];
		int[] yPoints = new int[6];

		for (int i = 0; i < 6; i++) {
			double angle = Math.toRadians(60 * i + 30);
			xPoints[i] = (int) (centerX + radius * Math.cos(angle));
			yPoints[i] = (int) (centerY + radius * Math.sin(angle));
		}

		return new Polygon(xPoints, yPoints, 6);
	}

	/**
	 * Displays the current available lots of hexagonal tiles and animals on the
	 * game screen.
	 *
	 * @param graphics the graphics context used for rendering
	 * @param startX   the X-coordinate of the starting position for displaying lots
	 * @param startY   the Y-coordinate of the starting position for displaying lots
	 * @throws NullPointerException if {@code graphics} is null
	 */
	public void showHexLot(Graphics2D graphics, int startX, int startY) {
		Objects.requireNonNull(graphics);
		var tileSize = 60;
		var lots = gameManager.getAvailableLot();
		int verticalSpacing = 40;
		int hexHeight = (int) (tileSize * Math.sqrt(3));

		for (int i = 0; i < lots.size(); i++) {
			var pair = lots.get(i);
			int centerX = startX;
			int centerY = startY + i * (hexHeight + verticalSpacing);

			drawHexTile(graphics, pair.tile(), centerX, centerY, tileSize, 0);
			drawAnimal(graphics, pair.animal(), centerX + tileSize, centerY, tileSize);
			lots.checkNumberFaune(gameManager.getAnimals());
		}
	}

	/**
	 * Displays the selected tile-animal pair on the game screen.
	 *
	 * @param graphics     the graphics context used for rendering
	 * @param selectedPair the selected tile-animal pair
	 * @param startX       the X-coordinate of the starting position
	 * @param startY       the Y-coordinate of the starting position
	 * @param tileSize     the size of the hexagonal tile
	 * @throws NullPointerException if {@code graphics} or {@code selectedPair} is
	 *                              null
	 */
	public void showSelectedLot(Graphics2D graphics, TileAnimalPair selectedPair, int startX, int startY, int tileSize) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(selectedPair);
		drawHexTile(graphics, selectedPair.tile(), startX, startY, tileSize,
				Math.toRadians(selectedPair.tile().getCurrentRotation()) * 60);
		drawAnimal(graphics, selectedPair.animal(), startX + tileSize, startY, tileSize);
	}

	private boolean isPointInHex(int x, int y, int centerX, int centerY, int tileSize) {
		double dx = Math.abs(x - centerX) / (double) tileSize;
		double dy = Math.abs(y - centerY) / (double) tileSize;

		return dy <= Math.sqrt(3) * (1 - dx);
	}

	/**
	 * Handles the player's interaction to select a hexagonal tile-animal pair by
	 * clicking.
	 *
	 * @param context         the application context for handling user events
	 * @param startX          the X-coordinate of the starting position for
	 *                        displaying lots
	 * @param startY          the Y-coordinate of the starting position for
	 *                        displaying lots
	 * @param tileSize        the size of the hexagonal tiles
	 * @param verticalSpacing the vertical spacing between displayed lots
	 * @param tiles           the {@link Tiles} object managing game tiles
	 * @param animals         the {@link FauneBag} containing the available animals
	 * @return the selected {@link TileAnimalPair}
	 * @throws NullPointerException if {@code context}, {@code tiles}, or
	 *                              {@code animals} is null
	 */
	public TileAnimalPair chooseHexLotClick(ApplicationContext context, int startX, int startY, int tileSize,
			int verticalSpacing, Tiles tiles, FauneBag animals) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(tiles);
		Objects.requireNonNull(animals);
		int hexHeight = (int) (tileSize * Math.sqrt(3));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event != null) {
				switch (event) {
				case PointerEvent e:
					if (e.action() == PointerEvent.Action.POINTER_DOWN) {
						var location = e.location();
						var xClick = (int) location.x();
						var yClick = (int) location.y();
						for (int i = 0; i < gameManager.getAvailableLot().size(); i++) {
							int centerX = startX, centerY = startY + i * (hexHeight + verticalSpacing);
							if (isPointInHex(xClick, yClick, centerX, centerY, tileSize)) {
								var choice = gameManager.getAvailableLot().get(i);
								gameManager.getAvailableLot().replaceSelectedLot(choice, tiles, animals);
								return choice;
							}
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
	 * Handles the player's interaction to select a tile by clicking.
	 *
	 * @param context         the application context for handling user events
	 * @param startX          the X-coordinate of the starting position for
	 *                        displaying tiles
	 * @param startY          the Y-coordinate of the starting position for
	 *                        displaying tiles
	 * @param tileSize        the size of the hexagonal tiles
	 * @param verticalSpacing the vertical spacing between displayed tiles
	 * @param tiles           the {@link Tiles} object managing game tiles
	 * @return the selected {@link Tile}
	 * @throws NullPointerException if {@code context} or {@code tiles} is null
	 */
	public Tile chooseTileClick(ApplicationContext context, int startX, int startY, int tileSize, int verticalSpacing,
			Tiles tiles) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(tiles);
		int hexHeight = (int) (tileSize * Math.sqrt(3));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event != null) {
				switch (event) {
				case PointerEvent e:
					if (e.action() == PointerEvent.Action.POINTER_DOWN) {
						var location = e.location();
						var xClick = (int) location.x();
						var yClick = (int) location.y();
						for (int i = 0; i < gameManager.getAvailableLot().size(); i++) {
							int centerX = startX, centerY = startY + i * (hexHeight + verticalSpacing);
							if (isPointInHex(xClick, yClick, centerX, centerY, tileSize)) {
								var choice = gameManager.getAvailableLot().get(i).tile();
								gameManager.getAvailableLot().replaceSelectedTile(i, tiles);
								return choice;
							}
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
	 * Handles the player's interaction to select an animal by clicking.
	 *
	 * @param context         the application context for handling user events
	 * @param startX          the X-coordinate of the starting position for
	 *                        displaying animals
	 * @param startY          the Y-coordinate of the starting position for
	 *                        displaying animals
	 * @param animalSize      the size of the animal images
	 * @param verticalSpacing the vertical spacing between displayed animals
	 * @param animals         the {@link FauneBag} containing the available animals
	 * @return the selected {@link Animal}
	 * @throws NullPointerException if {@code context} or {@code animals} is null
	 */
	public Animal chooseAnimalClick(ApplicationContext context, int startX, int startY, int animalSize,
			int verticalSpacing, FauneBag animals) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(animals);
		int hexHeight = (int) (animalSize * Math.sqrt(3));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event != null) {
				switch (event) {
				case PointerEvent e:
					if (e.action() == PointerEvent.Action.POINTER_DOWN) {
						var location = e.location();
						int xClick = (int) location.x(), yClick = (int) location.y();
						for (int i = 0; i < gameManager.getAvailableLot().size(); i++) {
							int animalX = startX, animalY = startY + i * (hexHeight + verticalSpacing);
							if (xClick >= animalX && xClick <= animalX + animalSize && yClick >= animalY
									&& yClick <= animalY + animalSize) {
								var choice = gameManager.getAvailableLot().get(i).animal();
								gameManager.getAvailableLot().replaceSelectedAnimal(i, animals);
								return choice;
							}
						}
					}
					break;
				default:
					break;
				}
			}
		}
	}

	private void drawNTbutton(Graphics2D graphics, int startX, int startY, int buttonWidth, int buttonHeight) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(startX, startY, buttonWidth, buttonHeight);
		graphics.setColor(Color.WHITE);
		graphics.drawRect(startX, startY, buttonWidth, buttonHeight);
		graphics.setFont(graphics.getFont().deriveFont(18f));
		graphics.drawString("Use Nature Token", startX + 10, startY + buttonHeight / 2 + 5);
	}

	private void drawRotationButton(Graphics2D graphics, int startX, int startY, int buttonWidth, int buttonHeight) {
		graphics.setColor(Color.PINK);
		graphics.fillRect(startX, startY, buttonWidth, buttonHeight);
		graphics.setColor(Color.BLACK);
		graphics.drawRect(startX, startY, buttonWidth, buttonHeight);
		graphics.setFont(graphics.getFont().deriveFont(18f));
		graphics.drawString("ROTATE", startX + 10, startY + buttonHeight / 2 + 5);
	}

	private void drawConfirmButton(Graphics2D graphics, int startX, int startY, int buttonWidth, int buttonHeight) {
		graphics.setColor(Color.GREEN);
		graphics.fillRect(startX, startY, buttonWidth, buttonHeight);
		graphics.setColor(Color.BLACK);
		graphics.drawRect(startX, startY, buttonWidth, buttonHeight);
		graphics.setFont(graphics.getFont().deriveFont(18f));
		graphics.drawString("CONFIRM", startX + 10, startY + buttonHeight / 2 + 5);
	}

	/**
	 * Handles the final choice of a lot (tile-animal pair) by a player, considering
	 * the use of nature tokens.
	 *
	 * @param context         the application context for handling user events
	 * @param startX          the X-coordinate for displaying lots
	 * @param startY          the Y-coordinate for displaying lots
	 * @param tileSize        the size of the hexagonal tiles
	 * @param verticalSpacing the vertical spacing between displayed lots
	 * @param name            the name of the player making the choice
	 * @param tiles           the {@link Tiles} object managing available tiles
	 * @param animals         the {@link FauneBag} containing the available animals
	 * @return the selected {@link TileAnimalPair}
	 * @throws NullPointerException if {@code context}, {@code name}, {@code tiles},
	 *                              or {@code animals} is {@code null}
	 */
	public TileAnimalPair finalLotChoice(ApplicationContext context, int startX, int startY, int tileSize,
			int verticalSpacing, String name, Tiles tiles, FauneBag animals) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(name);
		var player = gameManager.getPlayer(name);
		int buttonX = 600, buttonY = 5, buttonWidth = 200, buttonHeight = 30;
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event != null) {
				switch (event) {
				case PointerEvent e:
					if (e.action() == PointerEvent.Action.POINTER_DOWN) {
						var location = e.location();
						int xClick = (int) location.x(), yClick = (int) location.y();
						if (xClick >= buttonX && xClick <= buttonX + buttonWidth && yClick >= buttonY
								&& yClick <= buttonY + buttonHeight && player.getNatureTokens() > 0) {
							player.removeNatureToken();
							var chosenTile = chooseTileClick(context, startX, startY, tileSize, verticalSpacing, tiles);
							var chosenAnimal = chooseAnimalClick(context, startX + tileSize, startY, tileSize, verticalSpacing,
									animals);
							return new TileAnimalPair(chosenTile, chosenAnimal);
						} else {
							return chooseHexLotClick(context, startX, startY, tileSize, verticalSpacing, tiles, animals);
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
	 * Manages the rotation of a selected tile within a {@link TileAnimalPair}.
	 *
	 * @param context      the application context for handling user events
	 * @param selectedPair the selected tile-animal pair
	 * @return the final rotation angle of the tile
	 * @throws NullPointerException if {@code context} or {@code selectedPair} is
	 *                              {@code null}
	 */
	public double getRotation(ApplicationContext context, TileAnimalPair selectedPair) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(selectedPair);
		double angle = 0;
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event != null) {
				switch (event) {
				case PointerEvent e -> {
					if (e.action() == PointerEvent.Action.POINTER_DOWN) {
						var location = e.location();
						int xClick = (int) location.x(), yClick = (int) location.y();
						if (xClick >= 870 && xClick <= 870 + 100 && yClick >= 900 && yClick <= 900 + 50) {
							angle += 60;
							selectedPair.tile().rotate();
							context.renderFrame(graphics -> renderLot(graphics, selectedPair));
							if (angle >= 360) {
								angle = 0;
							}
						} else {
							return angle;
						}
						if (xClick >= 1000 && xClick <= 1000 + 100 && yClick >= 900 && yClick <= 900 + 50) {
							return angle;
						}
					}
				}
				default -> {
					return angle;
				}
				}
			}
		}
	}

	/**
	 * Handles the placement of a hexagonal tile by a player via mouse click.
	 *
	 * @param event    the event representing a mouse click
	 * @param tile     the tile to be placed
	 * @param name     the name of the player placing the tile
	 * @param startX   the X-coordinate for the board's starting position
	 * @param startY   the Y-coordinate for the board's starting position
	 * @param tileSize the size of the hexagonal tiles
	 * @return {@code 1} if the tile was successfully placed, otherwise {@code 0}
	 * @throws NullPointerException if {@code tile} or {@code name} is {@code null}
	 */
	public int placeHexTileClick(Event event, Tile tile, String name, int startX, int startY, int tileSize) {
		Objects.requireNonNull(tile);
		Objects.requireNonNull(name);
		if (event != null) {
			switch (event) {
			case PointerEvent e:
				if (e.action() == PointerEvent.Action.POINTER_DOWN) {
					var location = e.location();
					double clickX = location.x(), clickY = location.y();
					double dx = tileSize * Math.sqrt(3), dy = tileSize * 1.5;
					for (int row = -1; row <= gameManager.getPlayer(name).getBoard().getRowCount(); row++) {
						for (int col = -1; col <= gameManager.getPlayer(name).getBoard().getColCount(); col++) {
							double offset = (row % 2 == 0) ? 0 : dx / 2, centerX = startX + col * dx + offset;
							double centerY = startY + row * dy;
							if (isPointInHex((int) clickX, (int) clickY, (int) centerX, (int) centerY, tileSize)) {
								return gameManager.getPlayer(name).getBoard().placeTile(tile, row + 1, col + 1);
							}
						}
					}
				}
				break;
			default:
				break;
			}
		}
		return 0;
	}

	/**
	 * Handles the placement of an animal on the board by a player via mouse click.
	 *
	 * @param event    the event representing a mouse click
	 * @param animal   the animal to be placed
	 * @param name     the name of the player placing the animal
	 * @param startX   the X-coordinate for the board's starting position
	 * @param startY   the Y-coordinate for the board's starting position
	 * @param tileSize the size of the hexagonal tiles
	 * @return {@code 1} if the animal was successfully placed, otherwise {@code 0}
	 * @throws NullPointerException if {@code animal} or {@code name} is
	 *                              {@code null}
	 */
	public int placeAnimalClick(Event event, Animal animal, String name, int startX, int startY, int tileSize) {
		Objects.requireNonNull(animal);
		Objects.requireNonNull(name);
		if (event != null) {
			switch (event) {
			case PointerEvent e:
				if (e.action() == PointerEvent.Action.POINTER_DOWN) {
					var location = e.location();
					double clickX = location.x(), clickY = location.y();
					double dx = tileSize * Math.sqrt(3), dy = tileSize * 1.5;
					for (int row = 0; row < gameManager.getPlayer(name).getBoard().getRowCount(); row++) {
						for (int col = 0; col < gameManager.getPlayer(name).getBoard().getColCount(); col++) {
							double offset = (row % 2 == 0) ? 0 : dx / 2, centerX = startX + col * dx + offset,
									centerY = startY + row * dy;
							if (isPointInHex((int) clickX, (int) clickY, (int) centerX, (int) centerY, tileSize)) {
								int success = gameManager.getPlayer(name).getBoard().placeAnimal(animal, row + 1, col + 1);
								if (success == 1) {
									var tile = gameManager.getPlayer(name).getBoard().getTile(row, col);
									if (Lot.isIdealTile(tile)) {
										gameManager.getPlayer(name).addNatureToken();
										System.out.println(
												name + " gagne un jeton nature. Total : " + gameManager.getPlayer(name).getNatureTokens());
									}
								}
								return success;
							}
						}
					}
				}
				break;
			default:
				break;
			}
		}
		return 0;
	}

	/**
	 * Displays an error message when an animal cannot be placed on the board.
	 *
	 * @param graphics the graphics context used for rendering
	 * @param animal   the animal that cannot be placed
	 * @throws NullPointerException if {@code graphics} or {@code animal} is
	 *                              {@code null}
	 */
	public void animalPlacementError(Graphics2D graphics, Animal animal) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(animal);
		graphics.setFont(graphics.getFont().deriveFont(30f)); // Taille de police de 20
		graphics.drawString("Animal : " + animal + " can not be placed", 800, 600);

	}

	private void showAnimalsScore(Graphics2D graphics, Map<Animal, Integer> results, String name) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(results);
		Objects.requireNonNull(name);
		graphics.setColor(Color.BLACK);
		graphics.setFont(graphics.getFont().deriveFont(25f));

		int startX = 1000; // Position de départ pour dessiner le texte
		int startY = 100; // Position de départ pour dessiner le texte

		int yOffset = startY;
		graphics.drawString(name + " score:", startX, yOffset);
		yOffset += 25;
		for (var elem : results.entrySet()) {
			graphics.drawString(elem.getKey() + " Score: " + elem.getValue(), startX, yOffset);
			yOffset += 25;
		}
	}

	private void showTilesScore(Graphics2D graphics, Map<Habitat, Integer> tileScores, Map<Habitat, Integer> bonuses,
			String name) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(tileScores);
		Objects.requireNonNull(bonuses);
		Objects.requireNonNull(name);

		graphics.setColor(Color.BLACK);
		graphics.setFont(graphics.getFont().deriveFont(25f));

		int startX = 900; // Position de départ pour dessiner le texte
		int startY = 260; // Position de départ pour dessiner le texte

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

	private void showTokensScore(Graphics2D graphics, int natureTokenScore, String name) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(name);

		graphics.setColor(Color.BLACK);
		graphics.setFont(graphics.getFont().deriveFont(25f));

		int startX = 900; // Position de départ pour dessiner le texte
		int startY = 410; // Position de départ pour dessiner le texte
		graphics.drawString("Nature Token Score: " + natureTokenScore, startX, startY);

	}

	/**
	 * Displays the combined score of a player in Zen mode, including animal, tile,
	 * and nature token scores.
	 *
	 * @param graphics     the graphics context used for rendering
	 * @param animalScores a map containing scores for each animal type
	 * @param tileScores   a map containing scores for each habitat type
	 * @param bonuses      a map containing bonus scores for habitats
	 * @param natureToken  the number of nature tokens the player has
	 * @param name         the name of the player
	 * @throws NullPointerException if any parameter is {@code null}
	 */
	public void showCombinedScoreZen(Graphics2D graphics, Map<Animal, Integer> animalScores,
			Map<Habitat, Integer> tileScores, Map<Habitat, Integer> bonuses, int natureToken, String name) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(animalScores);
		Objects.requireNonNull(tileScores);
		Objects.requireNonNull(bonuses);
		Objects.requireNonNull(name);

		drawHexPlayerBoard(graphics, gameManager.getPlayer(name).getBoard(), 100, 100);
		showAnimalsScore(graphics, animalScores, name);
		showTilesScore(graphics, tileScores, bonuses, name);
		showTokensScore(graphics, natureToken, name);

		int totalScore = gameManager.getPlayer(name).getScoreCalculator().calculateTotalScore(animalScores, tileScores,
				bonuses, natureToken);
		graphics.drawString("Total Score: " + totalScore, 1500, 450);
	}

}