package fr.uge.cascadia.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import fr.uge.cascadia.view.ImageLoader;

/**
 * Represents a tile in the Cascadia game that contains specific habitats,
 * associated animals, and supports rotation for gameplay mechanics.
 *
 * <p>
 * The {@code Tile} class provides methods to manage habitats and animals,
 * rotate the tile, and determine its compatibility with other game elements.
 * </p>
 *
 * <h2>Structure of a Tile:</h2>
 * <ul>
 * <li>Each tile can have one or two habitats.</li>
 * <li>Each tile can support up to three animals.</li>
 * <li>Tiles are hexagonal and can be rotated.</li>
 * </ul>
 */
public final class Tile implements Lot {
	private final Habitat[] habitats;
	private final Animal[] animals;
	private int currentRotation; // Rotation actuelle (0-5)

	/**
	 * Constructs a {@code Tile} with the specified habitats and animals.
	 *
	 * @param habitats an array of {@link Habitat} representing the habitats of the
	 *                 tile.
	 * @param animals  an array of {@link Animal} associated with the tile.
	 * @throws NullPointerException     if either {@code habitats} or
	 *                                  {@code animals} is {@code null}.
	 * @throws IllegalArgumentException if the number of habitats is not 1 or 2, or
	 *                                  the number of animals exceeds the allowed
	 *                                  range (1-3).
	 */
	public Tile(Habitat[] habitats, Animal[] animals, int rotation) {
		Objects.requireNonNull(habitats, "Habitats cannot be null");
		Objects.requireNonNull(animals, "Animals cannot be null");

		if (habitats.length <= 0 || habitats.length > 2) {
			throw new IllegalArgumentException("Illegal number of habitats: " + habitats.length);
		}
		if (animals.length <= 0 || animals.length > 3) {
			throw new IllegalArgumentException("Illegal number of animals: " + animals.length);
		}

		this.habitats = habitats;
		this.animals = animals;
		this.currentRotation = rotation;
	}

	/**
	 * Generates the 6 sides of the hexagonal tile based on the current rotation.
	 *
	 * @return an array of 6 {@link Habitat} representing the sides of the tile.
	 */
	public Habitat[] sides(ImageLoader imageLoader) {
		Objects.requireNonNull(imageLoader);
		return calculateSides(imageLoader, currentRotation);
	}

	public Habitat[] calculateSides(ImageLoader imageLoader, int rotation) {
		Objects.requireNonNull(imageLoader);
		var sides = new Habitat[6];
		Arrays.fill(sides, null);

		if (habitats.length == 1) {
			Arrays.fill(sides, habitats[0]);
			return sides;
		}
		if (habitats.length == 2) {
			var leftRightImage = habitats[0].toString().toLowerCase() + "_" + habitats[1].toString().toLowerCase() + ".png";
			var rightLeftImage = habitats[1].toString().toLowerCase() + "_" + habitats[0].toString().toLowerCase() + ".png";

			Habitat leftHabitat;
			Habitat rightHabitat;
			if (imageLoader.getImage(leftRightImage) != null) {
				leftHabitat = habitats[0];
				rightHabitat = habitats[1];
			} else if (imageLoader.getImage(rightLeftImage) != null) {
				leftHabitat = habitats[1];
				rightHabitat = habitats[0];
			} else {
				throw new IllegalStateException("No image found for habitats: " + Arrays.toString(habitats));
			}

			for (int i = 0; i < 3; i++) {
				sides[(i + rotation) % 6] = rightHabitat;
				sides[(i + 3 + rotation) % 6] = leftHabitat;
			}
		}

		return sides;
	}

	/**
	 * Rotates the tile clockwise by one position.
	 */
	public void rotate() {
		currentRotation = (currentRotation + 1) % 6;
	}

	@Override
	public Tile clone() {
		return new Tile(Arrays.copyOf(this.habitats, this.habitats.length),
				Arrays.copyOf(this.animals, this.animals.length), this.currentRotation);
	}

	/**
	 * Returns the current rotation of the tile.
	 *
	 * @return the current rotation
	 */
	public int getCurrentRotation() {
		return currentRotation;
	}

	/**
	 * Retrieves the habitats associated with this tile.
	 *
	 * @return an array of {@link Habitat}.
	 */
	public Habitat[] getHabitats() {
		return habitats;
	}

	/**
	 * Retrieves the animals associated with this tile.
	 *
	 * @return an array of {@link Animal}.
	 */
	public Animal[] getAnimals() {
		return animals;
	}

	/**
	 * Checks if this instance is a tile.
	 *
	 * @return {@code true}, as this class represents a tile.
	 */
	@Override
	public boolean isTile() {
		return true;
	}

	/**
	 * Checks if this instance is a tile-animal pair.
	 *
	 * @return {@code false}, as this class does not represent a tile-animal pair.
	 */
	@Override
	public boolean isTileAnimalPair() {
		return false;
	}

	/**
	 * Returns this instance as a {@link Tile}.
	 *
	 * @return this tile.
	 */
	@Override
	public Tile asTile() {
		return this;
	}

	/**
	 * Throws an exception, as this instance cannot be cast to
	 * {@link TileAnimalPair}.
	 *
	 * @return nothing, always throws an exception.
	 * @throws UnsupportedOperationException if this method is called.
	 */
	@Override
	public TileAnimalPair asTileAnimalPair() {
		throw new UnsupportedOperationException("This instance is not a TileAnimalPair");
	}

	/**
	 * Checks if the tile can accept the specified animal.
	 *
	 * @param present the {@link Animal} to check.
	 * @return {@code true} if the tile can accept the animal; {@code false}
	 *         otherwise.
	 * @throws NullPointerException if {@code present} is {@code null}.
	 */
	@Override
	public boolean canAcceptAnimal(Animal present) {
		Objects.requireNonNull(present);
		for (var animal : animals) {
			if (animal.equals(present)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the tile has the specified habitat.
	 *
	 * @param habitat the {@link Habitat} to check.
	 * @return {@code true} if the tile contains the habitat; {@code false}
	 *         otherwise.
	 * @throws NullPointerException if {@code habitat} is {@code null}.
	 */
	@Override
	public boolean hasHabitat(ImageLoader imageLoader, Habitat habitat) {
		Objects.requireNonNull(habitat);
		for (var h : sides(imageLoader)) {
			if (h != null && h.equals(habitat)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		var sb = new StringBuilder();
		sb.append("┌─────┐").append("\n");
		sb.append("│  ").append(habitats[0]).append(" |").append("\n");
		sb.append("├──┬──┤").append("\n");

		String animal1 = (animals.length > 0) ? animals[0].toString() : " ";
		String animal2 = (animals.length > 1) ? animals[1].toString() : " ";
		sb.append("│").append(animal1).append(" |").append(animal2).append(" |").append("\n");

		sb.append("└──┴──┘");
		return sb.toString();
	}

	/**
	 * Generates a set of predefined tiles for the game with square tiles.
	 *
	 * @return an array of predefined {@code Tile} instances
	 */
	public static Tile[] predefinedTiles() {
		return new Tile[] { new Tile(new Habitat[] { Habitat.FOREST }, new Animal[] { Animal.BEAR, Animal.FOX }, 0),
				new Tile(new Habitat[] { Habitat.RIVER }, new Animal[] { Animal.SALMON, Animal.ELK }, 0),
				new Tile(new Habitat[] { Habitat.MOUNTAIN }, new Animal[] { Animal.BUZZARD, Animal.ELK }, 0),
				new Tile(new Habitat[] { Habitat.MARSH }, new Animal[] { Animal.SALMON, Animal.BUZZARD }, 0),
				new Tile(new Habitat[] { Habitat.MEADOW }, new Animal[] { Animal.FOX, Animal.ELK }, 0) };
	}

	/**
	 * Generates a set of 25 ideal tiles for the game with hexagonal tiles.
	 *
	 * @return a list of ideal hexagonal {@code Tile} instances
	 */
	public static List<Tile> idealTiles() {
		var idTiles = new ArrayList<Tile>();
		Random random = new Random();
		for (int i = 0; i < 25; i++) {
			var habitat = Habitat.values()[random.nextInt(Habitat.values().length)];
			var animal = Animal.values()[random.nextInt(Animal.values().length)];
			var tile = new Tile(new Habitat[] { habitat }, new Animal[] { animal }, 0);
			idTiles.add(tile.clone());
		}
		return idTiles;
	}

	/**
	 * Generates a set of hexagonal tiles with two habitats and 2 or 3 allowed
	 * animals selected randomly for the game with hexagonal tiles.
	 *
	 * @return a list of hexagonal {@code Tile} instances
	 */
	public static List<Tile> hexTiles() {
		var otherTiles = new ArrayList<Tile>();
		var random = new Random();
		for (int i = 0; i < 60; i++) {
			var habitat1 = Habitat.values()[random.nextInt(Habitat.values().length)];
			var habitat2 = Habitat.values()[random.nextInt(Habitat.values().length)];
			while (habitat2.equals(habitat1)) {
				habitat2 = Habitat.values()[random.nextInt(Habitat.values().length)];
			}
			int animalCount = random.nextInt(2) + 2;
			var selectedAnimals = new ArrayList<Animal>();
			while (selectedAnimals.size() < animalCount) {
				var animal = Animal.values()[random.nextInt(Animal.values().length)];
				if (!selectedAnimals.contains(animal)) {
					selectedAnimals.add(animal);
				}
			}
			var animalsArray = selectedAnimals.toArray(new Animal[0]);
			var tile = new Tile(new Habitat[] { habitat1, habitat2 }, animalsArray, 0);
			otherTiles.add(tile.clone());
		}
		return otherTiles;
	}
}