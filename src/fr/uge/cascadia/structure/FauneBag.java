package fr.uge.cascadia.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a bag containing a collection of animals in the Cascadia game.
 *
 * <p>The {@code FauneBag} class manages the inventory of animals, including adding animals,
 * retrieving the list of animals, and randomly selecting an animal.</p>
 */
public class FauneBag {
  private final Map<Animal, Integer> animals;
  private final Random random = new Random();

  /**
   * Constructs a new {@code FauneBag} and initializes it with 20 of each {@link Animal}.
   */
  public FauneBag() {
    animals = new HashMap<>();
    for (var animal : Animal.values()) {
      animals.put(animal, 20);
    }
  }

  /**
   * Adds an animal to the bag.
   *
   * @param animal the {@link Animal} to add
   */
  public void addAnimal(Animal animal) {
  	Objects.requireNonNull(animal);
    animals.put(animal, animals.getOrDefault(animal, 0) + 1);
  }

  /**
   * Returns a string representation of the animals in the bag.
   *
   * @return a string displaying the animals and their counts
   */
  @Override
  public String toString() {
    var sb = new StringBuilder();
    for (var animal : animals.entrySet()) {
      var key = animal.getKey();
      var value = animal.getValue();
      sb.append(key).append(" : ").append(value).append("\n");
    }
    return sb.toString();
  }

  /**
   * Retrieves the map of animals and their counts in the bag.
   *
   * @return a {@link Map} where the keys are {@link Animal} and values are their counts
   */
  public Map<Animal, Integer> getAnimals() {
    return animals;
  }

  /**
   * Returns a list of all distinct animals in the bag.
   *
   * @return a {@link List} of {@link Animal} currently in the bag
   */
  public List<Animal> animalList() {
    var animalsPresent = new ArrayList<Animal>();
    for (var entry : animals.entrySet()) {
      var animal = entry.getKey();
      animalsPresent.add(animal);
    }
    return animalsPresent;
  }

  /**
   * Selects a random animal from the bag and decreases its count.
   *
   * @return the randomly selected {@link Animal}
   * @throws IllegalStateException if the bag is empty
   */
  public Animal selectRandomAnimal() {
    if (animals.isEmpty()) {
      throw new IllegalStateException("Bag is empty. No animal available.");
    }
    var animalList = new ArrayList<Animal>(animals.keySet());
    int randomIndex = random.nextInt(animalList.size());
    var selectedAnimal = animalList.get(randomIndex);
    int currentCount = animals.get(selectedAnimal);
    if (currentCount > 0) {
      animals.put(selectedAnimal, currentCount - 1);
    } else {
      animals.remove(selectedAnimal);
    }
    return selectedAnimal;
  }
}
