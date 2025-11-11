package fr.uge.cascadia.score.cards;

import java.util.HashMap;
import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Implements the scoring rule for Foxes according to the "C" card in the Cascadia game.
 * 
 * <p>This rule calculates points based on the most frequent species surrounding 
 * each Fox on the board, excluding other Foxes.</p>
 * 
 * <p>Rules:</p>
 * <ul>
 *   <li>Each Fox scores points equal to the number of the most frequent species surrounding it.</li>
 *   <li>The maximum score for a single Fox is capped at 6 points.</li>
 * </ul>
 * 
 */
public record FoxScoreCardC() implements ScoreRule {

  /**
   * Calculates the score for the player based on the "C" Fox scoring rule.
   *
   * <p>For each Fox, the species surrounding it (excluding other Foxes) are counted,
   * and points are awarded based on the most frequent species.</p>
   *
   * @param player the player whose score is being calculated
   * @return the calculated score based on the frequency of the most common species surrounding each Fox
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);

    int score = 0;
    var foxGroups = player.getBoard().findAnimalGroups(Animal.FOX);

    for (var group : foxGroups) {
      for (var pair : group) { // Iterates over each TileAnimalPair containing a Fox
        var neighbors = player.getBoard().getNeighbors(pair);
        var speciesCount = new HashMap<Animal, Integer>();

        // Counts the species in neighboring tiles (excluding Foxes)
        for (var neighbor : neighbors) {
          if (neighbor.isTileAnimalPair()) { // Ensures the neighbor is a TileAnimalPair
            var neighborPair = neighbor.asTileAnimalPair();
            var neighborAnimal = neighborPair.animal();
            if (!neighborAnimal.equals(Animal.FOX)) { // Excludes Foxes
              speciesCount.put(neighborAnimal, speciesCount.getOrDefault(neighborAnimal, 0) + 1);
            }
          }
        }

        // Finds the most frequent species
        int maxCount = speciesCount.values().stream()
          .max(Integer::compare)
          .orElse(0);

        score += Math.min(maxCount, 6); // Caps the maximum score per Fox at 6
      }
    }

    return score;
  }
}
