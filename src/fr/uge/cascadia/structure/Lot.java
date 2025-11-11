package fr.uge.cascadia.structure;

import java.util.Objects;

import fr.uge.cascadia.view.ImageLoader;


/**
 * Represents a general lot in the Cascadia game, which can either be a {@link Tile} or a {@link TileAnimalPair}.
 * 
 * <p>The {@code Lot} interface provides methods to check the type of the lot, convert it to its specific type,
 * and check its compatibility with animals and habitats.</p>
 */
public sealed interface Lot permits Tile, TileAnimalPair {

  /**
   * Returns a string representation of the lot.
   *
   * @return a string describing the lot
   */
  @Override
  String toString();

  /**
   * Checks if the lot is a {@link Tile}.
   *
   * @return {@code true} if the lot is a {@link Tile}, {@code false} otherwise
   */
  boolean isTile();

  /**
   * Checks if the lot is a {@link TileAnimalPair}.
   *
   * @return {@code true} if the lot is a {@link TileAnimalPair}, {@code false} otherwise
   */
  boolean isTileAnimalPair();

  /**
   * Converts the lot to a {@link Tile}.
   *
   * @return the lot as a {@link Tile}
   * @throws ClassCastException if the lot is not a {@link Tile}
   */
  Tile asTile();

  /**
   * Converts the lot to a {@link TileAnimalPair}.
   *
   * @return the lot as a {@link TileAnimalPair}
   * @throws ClassCastException if the lot is not a {@link TileAnimalPair}
   */
  TileAnimalPair asTileAnimalPair();

  /**
   * Checks if the lot can accept the specified animal.
   *
   * @param animal the {@link Animal} to check compatibility with
   * @return {@code true} if the lot can accept the animal, {@code false} otherwise
   */
  boolean canAcceptAnimal(Animal animal);

  /**
   * Checks if the lot has the specified habitat.
   *
   * @param habitat the {@link Habitat} to check compatibility with
   * @return {@code true} if the lot has the habitat, {@code false} otherwise
   */
  boolean hasHabitat(ImageLoader imageLoader, Habitat habitat);
  
  /**
   * Determines if the provided {@link Lot} is an "ideal tile" in the Cascadia game.
   *
   * <p>An ideal tile is defined as:
   * <ul>
   *   <li>A {@link Tile} with exactly one habitat and one animal.</li>
   *   <li>A {@link TileAnimalPair} whose associated {@link Tile} has exactly one habitat and one animal.</li>
   * </ul>
   *
   * @param lot the {@link Lot} to check, which can be either a {@link Tile} or a {@link TileAnimalPair}
   * @return {@code true} if the provided {@link Lot} is an ideal tile; {@code false} otherwise
   * @throws NullPointerException if {@code lot} is {@code null}
   */
  public static boolean isIdealTile(Lot lot) {
    Objects.requireNonNull(lot);
    switch (lot) {
      case Tile t: return t.getHabitats().length == 1 && t.getAnimals().length == 1;
      case TileAnimalPair ta: return ta.tile().getHabitats().length == 1 && ta.tile().getAnimals().length == 1;
    }
  }
}

