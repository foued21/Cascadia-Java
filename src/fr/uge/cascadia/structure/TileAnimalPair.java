package fr.uge.cascadia.structure;

import java.util.Objects;

import fr.uge.cascadia.view.ImageLoader;

/**
 * Represents a pairing of a {@link Tile} and an {@link Animal}.
 * 
 * <p>
 * This class is used when a specific animal is placed on a tile in the Cascadia
 * game.
 * </p>
 * 
 * @param tile   the {@link Tile} on which the animal is placed
 * @param animal the {@link Animal} placed on the tile
 * @throws NullPointerException if {@code tile} or {@code animal} is
 *                              {@code null}
 */
public record TileAnimalPair(Tile tile, Animal animal) implements Lot {

	/**
	 * Constructs a {@code TileAnimalPair} with the specified tile and animal.
	 *
	 * @param tile   the tile on which the animal is placed
	 * @param animal the animal placed on the tile
	 */
	public TileAnimalPair {
		Objects.requireNonNull(tile, "Tile cannot be null");
		Objects.requireNonNull(animal, "Animal cannot be null");
	}

	/**
	 * Returns a string representation of the {@code TileAnimalPair} for
	 * visualization.
	 *
	 * @return a string describing the {@code TileAnimalPair}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("┌─────┐").append("\n");
		sb.append("│  ").append(tile.getHabitats()[0]).append(" |").append("\n");
		sb.append("├──┬──┤").append("\n");
		sb.append("│  ").append(animal).append("  |").append("\n");
		sb.append("└──┴──┘");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTile() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canAcceptAnimal(Animal animal) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tile asTile() {
		throw new UnsupportedOperationException("This instance is not a Tile");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileAnimalPair asTileAnimalPair() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTileAnimalPair() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasHabitat(ImageLoader imageLoader, Habitat habitat) {
		Objects.requireNonNull(habitat);
		Objects.requireNonNull(imageLoader);
		return tile.hasHabitat(imageLoader, habitat);
	}

}
