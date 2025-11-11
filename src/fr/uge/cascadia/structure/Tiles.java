package fr.uge.cascadia.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Manages the tiles in the Cascadia game.
 * 
 * <p>
 * This class provides functionality for initializing, shuffling, and selecting
 * tiles for gameplay. It also ensures the correct number of tiles are available
 * based on the number of players.
 * </p>
 */
public class Tiles {
	private final List<Tile> tiles;
	private final Random random = new Random();

	/**
	 * Constructs an empty {@code Tiles} instance.
	 */
	public Tiles() {
		this.tiles = new ArrayList<>();
	}

	/**
	 * Adds a tile to the collection.
	 *
	 * @param tile the tile to add
	 * @throws NullPointerException if {@code tile} is {@code null}
	 */
	public void add(Tile tile) {
		Objects.requireNonNull(tile);
		tiles.add(tile);
	}

	/**
	 * Initializes the tiles bag with predefined tiles. Each predefined tile is
	 * added multiple times to the collection.
	 */
	private void initTiles() {
		var predefined = Tile.predefinedTiles();
		for (var tile : predefined) {
			for (int j = 0; j < 17; j++) {
				tiles.add(tile);
			}
		}
	}

	/**
	 * Initializes the hexagonal tiles bag with ideal and normal tiles.
	 */
	private void initHexTiles() {
    var idealTiles = Tile.idealTiles();
    var hexagonalTiles = Tile.hexTiles();
    tiles.addAll(idealTiles);
    tiles.addAll(hexagonalTiles);
	}

	/**
	 * Initializes the predefined sets of first tiles for players.
	 *
	 * @return a list of predefined tile arrays
	 */
	public List<Tile[]> initFirstTiles() {
		var predefined = Tile.predefinedTiles();
		var firstTiles = new ArrayList<Tile[]>();
		firstTiles.add(new Tile[] { predefined[0], predefined[1], predefined[2] });
		firstTiles.add(new Tile[] { predefined[1], predefined[3], predefined[4] });
		firstTiles.add(new Tile[] { predefined[2], predefined[4], predefined[0] });
		firstTiles.add(new Tile[] { predefined[3], predefined[0], predefined[1] });
		firstTiles.add(new Tile[] { predefined[4], predefined[2], predefined[3] });
		return firstTiles;
	}

	/**
	 * Initializes the predefined sets of hexagonal first tiles for players.
	 *
	 * @return a list of predefined tile arrays
	 */
	public List<Tile[]> initHexFirstTiles() {
		var firstTiles = new ArrayList<Tile[]>();
    var tilesPool = tiles.stream().map(Tile::clone).collect(Collectors.toList());
		for (int i = 0; i < 5; i++) {
			var tileSet = new Tile[3];
			for (int j = 0; j < 3; j++) {
				var randomIndex = new Random().nextInt(tilesPool.size());
				tileSet[j] = tilesPool.remove(randomIndex);
			}
			firstTiles.add(tileSet);
		}
		return firstTiles;
	}

	/**
	 * Prepares the tiles for gameplay based on the number of players and the mode
	 * chosen for the game either square tiles or hexagonal tiles. Ensures the
	 * correct number of tiles are present in the collection.
	 *
	 * @param nbPlayers the number of players
	 * @throws IllegalArgumentException if the number of players is not 2
	 */
	public void gameTiles(int nbPlayers, boolean isHex) {
		if (!isHex) {
			initTiles();
		} else {
			initHexTiles();
		}
		if (nbPlayers <= 0 || nbPlayers > 4) {
			throw new IllegalArgumentException("Incorrect number of players");
		}
		int tilesToRemove = 85 - ((nbPlayers * 20) + 3);
		Collections.shuffle(tiles);

		for (int i = 0; i < tilesToRemove; i++) {
			tiles.remove(random.nextInt(tiles.size()));
		}
	}

	/**
	 * Selects a random set of first tiles for a player from a list.
	 *
	 * @param firstTiles the list of available first tile sets
	 * @return a random tile array
	 * @throws NullPointerException     if {@code firstTiles} is {@code null}
	 * @throws IllegalArgumentException if {@code firstTiles} is empty
	 */
	public Tile[] randomFirstTile(List<Tile[]> firstTiles) {
		Objects.requireNonNull(firstTiles, "First tiles list cannot be null");
		if (firstTiles.isEmpty()) {
			throw new IllegalArgumentException("First tiles list cannot be empty");
		}
		int randomIndex = random.nextInt(firstTiles.size());
		return firstTiles.get(randomIndex);
	}

	/**
	 * Selects a random tile from the collection. The selected tile is removed from
	 * the collection.
	 *
	 * @return the selected tile
	 * @throws IllegalArgumentException if the collection is empty
	 */
	public Tile selectRandomTile() {
		if (tiles.isEmpty()) {
			throw new IllegalArgumentException("The tile collection is empty");
		}
		int randomIndex = random.nextInt(tiles.size());
		Tile selectedTile = tiles.get(randomIndex);
		tiles.remove(randomIndex);
		return selectedTile;
	}

	/**
	 * Returns the number of tiles remaining in the collection.
	 *
	 * @return the size of the tile collection
	 */
	public int size() {
		return tiles.size();
	}


	@Override
	public String toString() {
		var sb = new StringBuilder();
		for (var tile : tiles) {
			sb.append(tile).append("\n");
		}
		return sb.toString();
	}
}