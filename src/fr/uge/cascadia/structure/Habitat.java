package fr.uge.cascadia.structure;

import java.util.List;

/**
 * Represents the different habitat types in the Cascadia game.
 * 
 * <p>Each habitat is identified by a unique display symbol for representation.</p>
 */
public enum Habitat {
  MOUNTAIN("MO"),
  FOREST("FO"),
  RIVER("RI"),
  MEADOW("PR"),
  MARSH("MA");

  private final String displayName;

  /**
   * Constructs a {@code Habitat} enum with the specified display symbol.
   *
   * @param displayName the symbol representing the habitat
   */
  Habitat(String displayName) {
      this.displayName = displayName;
  }

  /**
   * Returns a list of all habitats.
   * 
   * @return an unmodifiable list of all habitat types
   */
  public static List<Habitat> allHabitats() {
      return List.of(Habitat.values());
  }

  /**
   * Returns a string representation of the habitat.
   * 
   * <p>This string is typically a two-character symbol representing the habitat.</p>
   *
   * @return the display symbol of the habitat
   */
  @Override
  public String toString() {
      return displayName;
  }
}
