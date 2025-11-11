package fr.uge.cascadia.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents the collection of available tile-animal pairs (lots) in the Cascadia game.
 *
 * <p>The {@code GameLot} class manages the available pairs of tiles and animals that players can select
 * during their turn. It supports operations to initialize, replace, and check the lots.</p>
 */
public class GameLot {
  private final List<TileAnimalPair> availablePair;

  /**
   * Constructs an empty {@code GameLot}.
   */
  public GameLot() {
    this.availablePair = new ArrayList<>();
  }

  /**
   * Initializes the available pairs with randomly selected tiles and animals.
   *
   * @param tiles the {@link Tiles} source for tile selection
   * @param animals the {@link FauneBag} source for animal selection
   * @throws NullPointerException if {@code tiles} or {@code animals} is {@code null}
   */
  public void initAvailablePair(Tiles tiles, FauneBag animals) {
    Objects.requireNonNull(tiles);
    Objects.requireNonNull(animals);
    var selectedTiles = new Tile[4];
    for (int i = 0; i < 4; i++) {
      selectedTiles[i] = tiles.selectRandomTile();
    }
    var selectedAnimals = new Animal[4];
    for (int i = 0; i < 4; i++) {
      selectedAnimals[i] = animals.selectRandomAnimal();
    }
    for (int i = 0; i < selectedTiles.length; i++) {
      var pair = new TileAnimalPair(selectedTiles[i], selectedAnimals[i]);
      availablePair.add(pair);
    }
  }

  /**
   * Replaces a selected tile-animal pair with a new randomly selected pair.
   *
   * @param selectedPair the {@link TileAnimalPair} to replace
   * @param tiles the {@link Tiles} source for tile selection
   * @param animals the {@link FauneBag} source for animal selection
   * @throws NullPointerException if {@code selectedPair}, {@code tiles}, or {@code animals} is {@code null}
   */
  public void replaceSelectedLot(TileAnimalPair selectedPair, Tiles tiles, FauneBag animals) {
    Objects.requireNonNull(selectedPair);
    Objects.requireNonNull(tiles);
    Objects.requireNonNull(animals);
    int index = availablePair.indexOf(selectedPair);
    var newPair = new TileAnimalPair(tiles.selectRandomTile(), animals.selectRandomAnimal());
    availablePair.set(index, newPair);
  }

  public void replaceSelectedTile(int indexTile, Tiles tiles) {
    Objects.requireNonNull(tiles);
    var newTile = tiles.selectRandomTile();
    var currentPair = availablePair.get(indexTile);
    availablePair.set(indexTile, new TileAnimalPair(newTile, currentPair.animal()));
  }

  public void replaceSelectedAnimal(int indexAnimal, FauneBag animals) {
    Objects.requireNonNull(animals);
    var newAnimal = animals.selectRandomAnimal();
    var currentPair = availablePair.get(indexAnimal);
    availablePair.set(indexAnimal, new TileAnimalPair(currentPair.tile(),newAnimal));
  }

  public void replaceLotSolo(TileAnimalPair selectedPair, Tiles tiles, FauneBag animals) {
    Objects.requireNonNull(selectedPair);
    Objects.requireNonNull(tiles);
    Objects.requireNonNull(animals);

    var lastLot = availablePair.get(3);
    availablePair.remove(lastLot);

    var newTile1 = tiles.selectRandomTile();
    var newTile2 = tiles.selectRandomTile();
    var newFaune1 = animals.selectRandomAnimal();
    var newFaune2 = animals.selectRandomAnimal();

    availablePair.add(new TileAnimalPair(newTile1, newFaune1));
    availablePair.add(new TileAnimalPair(newTile2, newFaune2));
  }


  /**
   * Retrieves a tile-animal pair at the specified index.
   *
   * @param i the index of the desired {@link TileAnimalPair}
   * @return the {@link TileAnimalPair} at the specified index
   * @throws IndexOutOfBoundsException if the index is out of bounds
   */
  public TileAnimalPair get(int i) {
    if (i >= 0 && i < availablePair.size()) {
      return availablePair.get(i);
    } else {
      throw new IndexOutOfBoundsException("Index " + i + " is out of bounds.");
    }
  }

  /**
   * Returns the number of available tile-animal pairs.
   *
   * @return the size of the available pairs list
   */
  public int size() {
		return availablePair.size();
	}

  /**
   * Replaces all occurrences of a specific animal in the pairs with new animals.
   *
   * @param animal the {@link Animal} to replace
   * @param animals the {@link FauneBag} source for new animals
   * @throws NullPointerException if {@code animal} or {@code animals} is {@code null}
   */
  public void replaceAnimalInPair(Animal animal, FauneBag animals) {
  	Objects.requireNonNull(animal);
  	Objects.requireNonNull(animals);
    for (var pair : availablePair) {
      if (pair.animal().equals(animal)) {
        int index = availablePair.indexOf(pair);
        var newPair = new TileAnimalPair(pair.tile(), animals.selectRandomAnimal());
        availablePair.set(index, newPair);
      }
    }
  }

  /**
   * Prompts the player to decide whether to replace animals that occur too frequently.
   *
   * @param count the number of occurrences of the animal
   * @param animal the {@link Animal} to potentially replace
   * @return 1 if the player chooses to replace the animal, 0 otherwise
   * @throws NullPointerException if {@code animal} is {@code null}
   */
   private int playerChoice(int count, Animal animal) {
     Objects.requireNonNull(animal);
     System.out.println(this);
     System.out.println(count + " occurrences of animal: " + animal);
     System.out.println("Do you want to replace them? (1 for yes, 0 for no)");
     @SuppressWarnings("resource")
     var scanner = new Scanner(System.in);
     while (true) {
         System.out.print("Your choice: ");
         String input = scanner.nextLine().trim();
         try {
             int choice = Integer.parseInt(input);
             if (choice == 1 || choice == 0) {
                 return choice;
             } else {
                 System.out.println("Invalid choice. Please select either 1 (yes) or 0 (no).");
             }
         } catch (NumberFormatException e) {
             System.out.println("Invalid input. Please enter a valid number (1 or 0).");
         }
     }
 }


  /**
   * Checks for animals that occur too frequently in the available pairs and replaces them if needed.
   *
   * @param animals the {@link FauneBag} source for new animals
   * @throws NullPointerException if {@code animals} is {@code null}
   */
  public void checkNumberFaune(FauneBag animals) {
    Objects.requireNonNull(animals);

    var animalCounts = new HashMap<Animal, Integer>();
    for (var pair : availablePair) {
      animalCounts.merge(pair.animal(), 1, Integer::sum);
    }

    animalCounts.forEach((animal, count) -> {
      if (count == 4) {
        System.out.println("Automatically replacing animal: " + animal);
        replaceAnimalInPairMultiple(animal, animals, 4);
      } else if (count == 3) {
          int choice = playerChoice(count, animal);
          if (choice == 1) {
            replaceAnimalInPairMultiple(animal, animals, 3);
          } else {
            System.out.println("Keeping animal: " + animal);
          }
      }
    });
  }


  /**
   * Replaces an animal multiple times in the available pairs.
   *
   * @param animal the {@link Animal} to replace
   * @param animals the {@link FauneBag} source for new animals
   * @param count the number of occurrences to replace
   */
  private void replaceAnimalInPairMultiple(Animal animal, FauneBag animals, int count) {
    for (int i = 0; i < count; i++) {
      animals.addAnimal(animal);
    }
    replaceAnimalInPair(animal, animals);
  }



  /**
   * Returns a string representation of the available tile-animal pairs.
   *
   * @return a string describing the available pairs
   */
  @Override
  public String toString() {
    var sb = new StringBuilder();
    int i = 1;
    for (var pair : availablePair) {
      sb.append(i).append(": \n")
        .append("Tile: \n").append(pair.tile()).append("\t")
        .append("Animal: ").append(pair.animal()).append("\n\n");
      i++;
    }
    return sb.toString();
  }

}
