package fr.uge.cascadia.score.cards;

import java.util.HashMap;
import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Implements the scoring rule for Foxes according to the "B" card in the Cascadia game.
 *
 * <p>This rule calculates points based on the pairs of species surrounding each Fox on the board,
 * excluding other Foxes.</p>
 *
 * <p>Rules:</p>
 * <ul>
 *   <li>Each pair of the same species (excluding Foxes) adjacent to a Fox scores points.</li>
 *   <li>Points awarded:
 *     <ul>
 *       <li>1 pair: 3 points</li>
 *       <li>2 pairs: 5 points</li>
 *       <li>3 or more pairs: 7 points</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * @see FoxScoreRule
 */
public record FoxScoreCardB() implements ScoreRule {

  /**
   * Calculates the score for the player based on the "B" Fox scoring rule.
   *
   * <p>For each Fox, the species surrounding it (excluding other Foxes) are counted
   * in pairs, and points are awarded based on the number of pairs.</p>
   *
   * @param player the player whose score is being calculated
   * @return the calculated score based on the pairs of species surrounding each Fox
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

        // Calculates the number of pairs for each species
        int totalPairs = 0;
        for (int count : speciesCount.values()) {
          totalPairs += count / 2; // Each 2 animals of the same species form a pair
        }

        // Assign points based on the total number of pairs
        if (totalPairs == 1) score += 3;
        else if (totalPairs == 2) score += 5;
        else if (totalPairs >= 3) score += 7;
      }
    }

    return score;
  }
}
