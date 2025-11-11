package fr.uge.cascadia.structure;

/**
 * Represents the different animal species in the Cascadia game.
 * 
 * <p>Each animal is identified by a unique display symbol for representation.</p>
 */
public enum Animal {
  BEAR("O"),
  SALMON("S"),
  FOX("R"),
  ELK("W"),
  BUZZARD("B");

  private final String displayName;

  /**
   * Constructs an {@code Animal} enum with the specified display symbol.
   *
   * @param displayName the symbol representing the animal
   */
  Animal(String displayName) {
      this.displayName = displayName;
  }

  /**
   * Returns a string representation of the animal.
   * 
   * <p>This string is typically a one-character symbol representing the animal.</p>
   *
   * @return the display symbol of the animal
   */
  @Override
  public String toString() {
      return displayName;
  }
}
