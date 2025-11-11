package fr.uge.cascadia.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.Habitat;
import fr.uge.cascadia.structure.Lot;
import fr.uge.cascadia.structure.Tile;
import fr.uge.cascadia.structure.TileAnimalPair;
import fr.uge.cascadia.view.ImageLoader;

/**
 * Represents the game environment for Cascadia.
 *
 * <p>
 * The environment manages the game board and provides methods to place tiles,
 * animals, and calculate groups and scores for habitats and animals.
 * </p>
 */
public class Environnement {
	private final List<List<Lot>> gamePlate;
	private final ImageLoader imageLoader;
	private final boolean isHexagonal;

	/**
	 * Initializes the game board with the specified dimensions.
	 *
	 * @param width  the width of the board
	 * @param height the height of the board
	 */
	public Environnement(int width, int height, boolean isHexagonal) {
		gamePlate = new ArrayList<>();
		for (int i = 0; i < width; i++) {
			var row = new ArrayList<Lot>();
			for (int j = 0; j < height; j++) {
				row.add(null);
			}
			gamePlate.add(row);
		}
		this.imageLoader = new ImageLoader();
		this.imageLoader.preloadImages();
		this.isHexagonal = isHexagonal;
	}

	/**
	 * Returns the number of rows in the environment.
	 *
	 * @return the number of rows
	 */
	public int getRowCount() {
		return gamePlate.size();
	}

	/**
	 * Returns the number of columns in the environment. Assumes that all rows have
	 * the same number of columns.
	 *
	 * @return the number of columns, or 0 if there are no rows
	 */
	public int getColCount() {
		return gamePlate.isEmpty() ? 0 : gamePlate.get(0).size();
	}

	/**
	 * Places the first three tiles in the center of the board.
	 *
	 * @param firstTile an array of three tiles to place
	 * @throws NullPointerException     if {@code firstTile} is null
	 * @throws IllegalArgumentException if {@code firstTile.length} is not 3
	 */
	public void putFirstTile(Tile[] firstTile) {
		Objects.requireNonNull(firstTile);
		if (firstTile.length != 3) {
			throw new IllegalArgumentException("Invalid first tile.");
		}
		int centerRow = (gamePlate.size() / 2) - 1;
		int centerCol = (gamePlate.get(0).size() / 2) - 1;

		gamePlate.get(centerRow).set(centerCol, firstTile[0]);
		gamePlate.get(centerRow).set(centerCol + 1, firstTile[1]);
		gamePlate.get(centerRow + 1).set(centerCol + 1, firstTile[2]);
	}

	/**
	 * Retrieves the tile or lot at the specified coordinates.
	 *
	 * @param x the row coordinate
	 * @param y the column coordinate
	 * @return the {@link Lot} at the specified position or {@code null} if empty
	 */
	public Lot getTile(int x, int y) {
		if (x >= 0 && x < gamePlate.size() && y >= 0 && y < gamePlate.get(0).size()) {
			return gamePlate.get(x).get(y);
		}
		return null;
	}

	/**
	 * Checks if a tile can be placed at the specified coordinates.
	 *
	 * @param x the row coordinate
	 * @param y the column coordinate
	 * @return {@code true} if placement is allowed; {@code false} otherwise
	 */
	public boolean placementAllowed(int x, int y) {
		if (gamePlate.get(x).get(y) != null) {
			return false;
		}
		var neighbors = isHexagonal ? hexNeighborsCoord(x, y) : rectNeighborsCoord(x, y);
		for (int[] neighbor : neighbors) {
			int adjX = neighbor[0], adjY = neighbor[1];
			if (adjX >= 0 && adjX < gamePlate.size() && adjY >= 0 && adjY < gamePlate.get(adjX).size()
					&& gamePlate.get(adjX).get(adjY) != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the neighbors for rectangular grids.
	 *
	 * @param x the row coordinate
	 * @param y the column coordinate
	 * @return a list of neighbors' coordinates
	 */
	private List<int[]> rectNeighborsCoord(int x, int y) {
		int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } // Haut, bas, gauche, droite
		};
		var neighbors = new ArrayList<int[]>();
		for (int[] dir : directions) {
			int newX = x + dir[0];
			int newY = y + dir[1];
			neighbors.add(new int[] { newX, newY });
		}
		return neighbors;
	}

	/**
	 * Gets the neighbors for hexagonal grids.
	 *
	 * @param x the row coordinate
	 * @param y the column coordinate
	 * @return a list of neighbors' coordinates
	 */
	private List<int[]> hexNeighborsCoord(int x, int y) {
		int[][] directions = (x % 2 == 0) ? new int[][] { { -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, 0 }, { 1, -1 }, { 0, 1 } }
				: new int[][] { { -1, 0 }, { -1, 1 }, { 0, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 } };
		var neighbors = new ArrayList<int[]>();
		for (int[] dir : directions) {
			int newX = x + dir[0];
			int newY = y + dir[1];
			neighbors.add(new int[] { newX, newY });
		}
		return neighbors;
	}

	/**
	 * Ensures the game board has enough capacity to place a tile at the specified
	 * coordinates.
	 *
	 * @param x the row coordinate
	 * @param y the column coordinate
	 */
	public void ensureCapacity(int x, int y) {
		if (gamePlate.isEmpty()) {
			var initialRow = new ArrayList<Lot>();
			for (int i = 0; i <= y; i++) {
				initialRow.add(null);
			}
			gamePlate.add(initialRow);
		}

		while (x < 0) {
			var newRow = new ArrayList<Lot>();
			for (int i = 0; i < gamePlate.get(0).size(); i++) {
				newRow.add(null);
			}
			gamePlate.add(0, newRow);
			x++;
		}

		while (x >= gamePlate.size()) {
			var newRow = new ArrayList<Lot>();
			for (int i = 0; i < gamePlate.get(0).size(); i++) {
				newRow.add(null);
			}
			gamePlate.add(newRow);
		}

		while (y < 0) {
			for (var row : gamePlate) {
				row.add(0, null);
			}
			y++;
		}

		while (y >= gamePlate.get(0).size()) {
			for (var row : gamePlate) {
				row.add(null);
			}
		}
	}

	/**
	 * Places a tile on the board at the specified coordinates.
	 *
	 * @param tile the tile to place
	 * @param x    the row coordinate
	 * @param y    the column coordinate
	 * @return 1 if placement is successful, 0 otherwise
	 * @throws NullPointerException if {@code tile} is null
	 */
	public int placeTile(Tile tile, int x, int y) {
		Objects.requireNonNull(tile);
		x = x - 1;
		y = y - 1;
		ensureCapacity(x, y);
		if (!placementAllowed(x, y)) {
			System.out.println("Invalid position: (" + (x + 1) + ", " + (y + 1) + ")");
			return 0;
		}
		if (gamePlate.get(x).get(y) != null) {
			System.out.println("Occupied placement at (" + (x + 1) + ", " + (y + 1) + ")");
			return 0;
		}
		gamePlate.get(x).set(y, tile);
		System.out.println("Tile placed at (" + (x + 1) + ", " + (y + 1) + ")");
		return 1;
	}

	/**
	 * Checks if an animal can be placed on the board.
	 *
	 * @param animal the animal to check
	 * @return {@code true} if placement is possible; {@code false} otherwise
	 */
	public boolean canPlaceAnimal(Animal animal) {
		Objects.requireNonNull(animal);
		for (var row : gamePlate) {
			for (var lot : row) {
				if (lot != null && lot.canAcceptAnimal(animal)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Places an animal on a tile at the specified coordinates.
	 *
	 * @param animal the animal to place
	 * @param x      the row coordinate
	 * @param y      the column coordinate
	 * @return 1 if placement is successful, 0 otherwise
	 * @throws NullPointerException if {@code animal} is {@code null}
	 */
	public int placeAnimal(Animal animal, int x, int y) {
		Objects.requireNonNull(animal);
		var tileInPlace = getTile(x - 1, y - 1);
		if (tileInPlace == null) {
			System.out.println("Invalid position (" + x + ", " + y + ") for animal: " + animal);
			return 0;
		}
		if (tileInPlace.canAcceptAnimal(animal)) {
			var finalTile = new TileAnimalPair(tileInPlace.asTile(), animal);
			gamePlate.get(x - 1).set(y - 1, finalTile);
			System.out.println("Animal " + animal + " placed in (" + x + ", " + y + ")");
			return 1;
		} else {
			System.out.println("Animal: " + animal + " is invalid in tile at (" + x + ", " + y + ")");
			return 0;
		}
	}

	/**
	 * Finds all groups of {@link TileAnimalPair} for a specific animal on the
	 * board.
	 *
	 * @param animal the animal whose groups are being searched
	 * @return a list of groups, where each group is a list of
	 *         {@link TileAnimalPair}
	 * @throws NullPointerException if {@code animal} is {@code null}
	 */
	public List<List<TileAnimalPair>> findAnimalGroups(Animal animal) {
		Objects.requireNonNull(animal);
		var animalGroups = new ArrayList<List<TileAnimalPair>>();
		var visited = new HashSet<String>(); // Tracks visited coordinates as "x-y"

		for (int i = 0; i < gamePlate.size(); i++) {
			for (int j = 0; j < gamePlate.get(i).size(); j++) {
				Lot lot = gamePlate.get(i).get(j);
				if (lot != null && lot.isTileAnimalPair()) {
					var pair = lot.asTileAnimalPair();
					String coordinates = i + "-" + j;
					if (pair.animal().equals(animal) && !visited.contains(coordinates)) {
						var group = new ArrayList<TileAnimalPair>();
						if (isHexagonal) {
							dfsHexAnimal(i, j, group, visited, animal);
						} else {
							dfsSquareAnimal(i, j, group, visited, animal);
						}
						animalGroups.add(group);
					}
				}
			}
		}
		return animalGroups;
	}

	/**
	 * Performs a Depth-First Search (DFS) on a square grid to find connected groups
	 * of tiles containing the same animal.
	 *
	 * @param x       the x-coordinate of the starting tile
	 * @param y       the y-coordinate of the starting tile
	 * @param group   a {@link List} to collect the connected {@link TileAnimalPair}
	 *                instances
	 * @param visited a {@link Set} to track the visited coordinates
	 * @param animal  the {@link Animal} being searched for
	 * @throws NullPointerException if {@code group}, {@code visited}, or
	 *                              {@code animal} is {@code null}
	 */
	private void dfsSquareAnimal(int x, int y, List<TileAnimalPair> group, Set<String> visited, Animal animal) {
		Objects.requireNonNull(group);
		Objects.requireNonNull(visited);
		Objects.requireNonNull(animal);
		if (x < 0 || x >= gamePlate.size() || y < 0 || y >= gamePlate.get(x).size())
			return;

		Lot lot = gamePlate.get(x).get(y);
		if (lot == null)
			return;

		var pair = lot.asTileAnimalPair();
		var coordinates = x + "-" + y;
		if (pair == null || !pair.animal().equals(animal) || visited.contains(coordinates))
			return;

		visited.add(coordinates);
		group.add(pair);

		dfsSquareAnimal(x + 1, y, group, visited, animal);
		dfsSquareAnimal(x - 1, y, group, visited, animal);
		dfsSquareAnimal(x, y + 1, group, visited, animal);
		dfsSquareAnimal(x, y - 1, group, visited, animal);
	}

	/**
	 * Performs a Depth-First Search (DFS) on a hexagonal grid to find connected
	 * groups of tiles containing the same animal.
	 *
	 * @param x       the x-coordinate of the starting tile
	 * @param y       the y-coordinate of the starting tile
	 * @param group   a {@link List} to collect the connected {@link TileAnimalPair}
	 *                instances
	 * @param visited a {@link Set} to track the visited coordinates
	 * @param animal  the {@link Animal} being searched for
	 */
	private void dfsHexAnimal(int x, int y, List<TileAnimalPair> group, Set<String> visited, Animal animal) {
		if (x < 0 || x >= gamePlate.size() || y < 0 || y >= gamePlate.get(x).size())
			return;

		Lot lot = gamePlate.get(x).get(y);
		if (lot == null || !lot.isTileAnimalPair())
			return;

		var pair = lot.asTileAnimalPair();
		var coordinates = x + "-" + y;
		if (!pair.animal().equals(animal) || visited.contains(coordinates))
			return;

		visited.add(coordinates);
		group.add(pair);

		for (var neighbor : hexNeighborsCoord(x, y)) {
			dfsHexAnimal(neighbor[0], neighbor[1], group, visited, animal);
		}
	}

	/**
	 * Finds the coordinates of a specific {@link Lot} on the board.
	 *
	 * @param lotToFind the lot to locate
	 * @return an array of two integers representing the row and column of the lot
	 * @throws NullPointerException     if {@code lotToFind} is {@code null}
	 * @throws IllegalArgumentException if the lot is not found on the board
	 */
	public int[] getCoordinates(Lot lotToFind) {
		Objects.requireNonNull(lotToFind);
		for (int x = 0; x < gamePlate.size(); x++) {
			for (int y = 0; y < gamePlate.get(x).size(); y++) {
				var lot = gamePlate.get(x).get(y);
				if (lot != null && lot.equals(lotToFind)) {
					return new int[] { x, y };
				}
			}
		}
		throw new IllegalArgumentException("The specified lot does not exist on the game board.");
	}

	/**
	 * Checks if two {@link Lot} objects are adjacent on the board.
	 *
	 * @param lot1 the first lot
	 * @param lot2 the second lot
	 * @return {@code true} if the lots are adjacent; {@code false} otherwise
	 * @throws NullPointerException if {@code lot1} or {@code lot2} is {@code null}
	 */
	public boolean isAdjacent(Lot lot1, Lot lot2) {
		Objects.requireNonNull(lot1);
		Objects.requireNonNull(lot2);

		int[] coord1 = getCoordinates(lot1);
		int[] coord2 = getCoordinates(lot2);

		int dx = Math.abs(coord1[0] - coord2[0]);
		int dy = Math.abs(coord1[1] - coord2[1]);

		return (dx + dy == 1);
	}

	/**
	 * Finds the largest group of connected tiles with the same habitat.
	 *
	 * @param habitat the habitat type being searched
	 * @return the size of the largest habitat group
	 * @throws NullPointerException if {@code habitat} is {@code null}
	 */
	public int findLargestHabitatGroup(Habitat habitat) {
		Objects.requireNonNull(habitat);

		var visited = new HashSet<String>();
		int largestGroupSize = 0;

		for (int i = 0; i < gamePlate.size(); i++) {
			for (int j = 0; j < gamePlate.get(i).size(); j++) {
				Lot lot = gamePlate.get(i).get(j);
				String coordinates = i + "-" + j;

				if (lot != null && lot.hasHabitat(imageLoader, habitat) && !visited.contains(coordinates)) {
					var group = new ArrayList<Lot>();
					if (isHexagonal) {
						dfsHexHabitat(i, j, group, visited, habitat);
					} else {

						dfsSquareHabitat(i, j, group, visited, habitat);
					}
					largestGroupSize = Math.max(largestGroupSize, group.size());
				}
			}
		}
		return largestGroupSize;
	}

	/**
	 * Performs a Depth-First Search (DFS) on a square grid to find all connected
	 * tiles with a specified habitat.
	 *
	 * @param x       the starting row coordinate
	 * @param y       the starting column coordinate
	 * @param group   a {@link List} to collect the connected {@link Lot} instances
	 * @param visited a {@link Set} to track visited coordinates
	 * @param habitat the {@link Habitat} being searched for
	 * @throws NullPointerException if {@code group}, {@code visited}, or
	 *                              {@code habitat} is {@code null}
	 */
	private void dfsSquareHabitat(int x, int y, List<Lot> group, Set<String> visited, Habitat habitat) {
		Objects.requireNonNull(group);
		Objects.requireNonNull(visited);
		Objects.requireNonNull(habitat);
		if (x < 0 || x >= gamePlate.size() || y < 0 || y >= gamePlate.get(x).size())
			return;

		var lot = gamePlate.get(x).get(y);
		var coordinates = x + "-" + y;
		if (lot == null || !lot.hasHabitat(imageLoader, habitat) || visited.contains(coordinates))
			return;

		visited.add(coordinates);
		group.add(lot);

		dfsSquareHabitat(x + 1, y, group, visited, habitat);
		dfsSquareHabitat(x - 1, y, group, visited, habitat);
		dfsSquareHabitat(x, y + 1, group, visited, habitat);
		dfsSquareHabitat(x, y - 1, group, visited, habitat);
	}

	/**
	 * Performs a Depth-First Search (DFS) on a hexagonal grid to find all connected
	 * tiles with a specified habitat.
	 *
	 * @param x       the starting row coordinate
	 * @param y       the starting column coordinate
	 * @param group   a {@link List} to collect the connected {@link Lot} instances
	 * @param visited a {@link Set} to track visited coordinates
	 * @param habitat the {@link Habitat} being searched for
	 * @throws NullPointerException if {@code group}, {@code visited}, or
	 *                              {@code habitat} is {@code null}
	 */
	private void dfsHexHabitat(int x, int y, List<Lot> group, Set<String> visited, Habitat habitat) {
		Objects.requireNonNull(group);
		Objects.requireNonNull(visited);
		Objects.requireNonNull(habitat);

		if (x < 0 || x >= gamePlate.size() || y < 0 || y >= gamePlate.get(x).size())
			return;

		var lot = gamePlate.get(x).get(y);
		var coordinates = x + "-" + y;

		if (lot == null || !lot.hasHabitat(imageLoader, habitat) || visited.contains(coordinates))
			return;

		visited.add(coordinates);
		group.add(lot);

		for (var neighbor : hexNeighborsCoord(x, y)) {
			int neighborX = neighbor[0];
			int neighborY = neighbor[1];
			String neighborCoordinates = neighborX + "-" + neighborY;

			if (neighborX < 0 || neighborX >= gamePlate.size() || neighborY < 0
					|| neighborY >= gamePlate.get(neighborX).size()) {
				continue;
			}

			var neighborLot = getNeighborLot(neighborX, neighborY);
			if (neighborLot != null && !visited.contains(neighborCoordinates)) {
				if (areSidesAdjacent(lot, neighborLot, x, y, neighborX, neighborY)) {
					dfsHexHabitat(neighborX, neighborY, group, visited, habitat);
				}

			}
		}
	}

	/**
	 * Checks whether two {@link Lot} objects have adjacent sides based on their
	 * coordinates.
	 *
	 * @param currentLot  the current {@link Lot} on the board
	 * @param neighborLot the neighboring {@link Lot} to check
	 * @param currentX    the x-coordinate of the current lot
	 * @param currentY    the y-coordinate of the current lot
	 * @param neighborX   the x-coordinate of the neighboring lot
	 * @param neighborY   the y-coordinate of the neighboring lot
	 * @return {@code true} if the sides of the two lots are adjacent, otherwise
	 *         {@code false}
	 */
	private boolean areSidesAdjacent(Lot currentLot, Lot neighborLot, int currentX, int currentY, int neighborX,
			int neighborY) {
		var currentSides = extractSides(currentLot);
		var neighborSides = extractSides(neighborLot);

		if (currentSides == null || neighborSides == null)
			return false;

		var indices = calculateSideIndices(currentX, currentY, neighborX, neighborY);
		int currentSideIndex = indices[0];
		int neighborSideIndex = indices[1];

		return currentSides[currentSideIndex] != null && neighborSides[neighborSideIndex] != null
				&& currentSides[currentSideIndex].equals(neighborSides[neighborSideIndex]);
	}

	/**
	 * Extracts the habitat sides from a {@link Lot}.
	 *
	 * @param lot the {@link Lot} from which to extract habitat sides
	 * @return an array of {@link Habitat} representing the sides of the lot
	 * @throws IllegalArgumentException if the {@code lot} is not of a supported
	 *                                  type
	 */
	private Habitat[] extractSides(Lot lot) {
		Objects.requireNonNull(lot);
		if (lot.isTile()) {
			return lot.asTile().sides(imageLoader);
		} else if (lot.isTileAnimalPair()) {
			return lot.asTileAnimalPair().tile().sides(imageLoader);
		}
		throw new IllegalArgumentException("Unsupported Lot type");
	}

	/**
	 * Retrieves the neighboring {@link Lot} on the board at the specified
	 * coordinates.
	 *
	 * @param neighborX the x-coordinate of the neighbor
	 * @param neighborY the y-coordinate of the neighbor
	 * @return the neighboring {@link Lot}, or {@code null} if no neighbor exists
	 */
	private Lot getNeighborLot(int neighborX, int neighborY) {
		if (neighborX < 0 || neighborX >= gamePlate.size() || neighborY < 0
				|| neighborY >= gamePlate.get(neighborX).size()) {
			return null;
		}
		return gamePlate.get(neighborX).get(neighborY);
	}

	/**
	 * Calculates the indices of the sides that are adjacent between two lots based
	 * on their positions.
	 *
	 * @param currentX  the x-coordinate of the current lot
	 * @param currentY  the y-coordinate of the current lot
	 * @param neighborX the x-coordinate of the neighboring lot
	 * @param neighborY the y-coordinate of the neighboring lot
	 * @return an array of two integers: the side index of the current lot and the
	 *         side index of the neighboring lot
	 * @throws IllegalArgumentException if the neighbor coordinates are not valid
	 */
	private int[] calculateSideIndices(int currentX, int currentY, int neighborX, int neighborY) {
		boolean isEvenRow = (currentX % 2 == 0);

		if (neighborX == currentX - 1 && neighborY == currentY - (isEvenRow ? 1 : 0)) { // haut gauche
			return new int[] { 5, 2 };
		} else if (neighborX == currentX - 1 && neighborY == currentY + (isEvenRow ? 0 : 1)) { // haut droit
			return new int[] { 0, 3 };
		} else if (neighborX == currentX && neighborY == currentY + 1) { // droite
			return new int[] { 1, 4 };
		} else if (neighborX == currentX + 1 && neighborY == currentY + (isEvenRow ? 0 : 1)) { // bas droit
			return new int[] { 2, 5 };
		} else if (neighborX == currentX + 1 && neighborY == currentY - (isEvenRow ? 1 : 0)) { // bas gauche
			return new int[] { 3, 0 };
		} else if (neighborX == currentX && neighborY == currentY - 1) { // gauche
			return new int[] { 4, 1 };
		}

		throw new IllegalArgumentException(
				"the coordinates of the neighbor (" + neighborX + ", " + neighborY + " are not valid !");
	}

	/**
	 * Retrieves the neighbors of a given {@link Lot} on the board.
	 *
	 * @param lot the lot whose neighbors are being searched
	 * @return a list of adjacent lots
	 * @throws NullPointerException if {@code lot} is {@code null}
	 */
	public List<Lot> getNeighbors(Lot lot) {
		Objects.requireNonNull(lot);
		var neighbors = new ArrayList<Lot>();
		int[] coord = getCoordinates(lot);

		int x = coord[0];
		int y = coord[1];

		if (x > 0 && gamePlate.get(x - 1).get(y) != null) {
			neighbors.add(gamePlate.get(x - 1).get(y));
		}
		if (x < gamePlate.size() - 1 && gamePlate.get(x + 1).get(y) != null) {
			neighbors.add(gamePlate.get(x + 1).get(y));
		}
		if (y > 0 && gamePlate.get(x).get(y - 1) != null) {
			neighbors.add(gamePlate.get(x).get(y - 1));
		}
		if (y < gamePlate.get(x).size() - 1 && gamePlate.get(x).get(y + 1) != null) {
			neighbors.add(gamePlate.get(x).get(y + 1));
		}

		return neighbors;
	}

	private String[] showEmptyTile() {
		return new String[] { "┌─────┐", "│     │", "├──┬──┤", "│  |  │", "└──┴──┘" };
	}

	private StringBuilder[] tileLineBuilders() {
		return new StringBuilder[] { new StringBuilder(), new StringBuilder(), new StringBuilder(), new StringBuilder(),
				new StringBuilder() };
	}

	private String showRow(List<Lot> row) {
		Objects.requireNonNull(row);
		var emptyTile = showEmptyTile();
		var lineBuilders = tileLineBuilders();
		for (var tile : row) {
			var tileLines = (tile != null) ? tile.toString().split("\n") : emptyTile;
			for (int i = 0; i < tileLines.length; i++) {
				lineBuilders[i].append(tileLines[i]).append(" ");
			}
		}
		return String.join("\n", lineBuilders) + "\n";
	}

	@Override
	public String toString() {
		var sb = new StringBuilder();
		for (var row : gamePlate) {
			sb.append(showRow(row));
		}
		return sb.toString();
	}

}
