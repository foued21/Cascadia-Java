package fr.uge.cascadia.score.cards;

import java.util.HashSet;
import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Implements the scoring rule for Foxes according to the "A" card in the Cascadia game.
 *
 * <p>This rule calculates points based on the number of unique animal species surrounding each Fox
 * on the board.</p>
 *
 * <p>Rules:</p>
 * <ul>
 *   <li>Each unique species (including other Foxes) adjacent to a Fox scores 1 point.</li>
 *   <li>Maximum of 5 points per Fox.</li>
 * </ul>
 *
 * @see FoxScoreRule
 */
public record FoxScoreCardA() implements ScoreRule {

  /**
   * Calculates the score for the player based on the "A" Fox scoring rule.
   *
   * <p>For each Fox, the unique species surrounding it (including other Foxes) are counted,
   * and points are awarded based on the number of unique species, up to a maximum of 5 points per Fox.</p>
   *
   * @param player the player whose score is being calculated
   * @return the calculated score based on the unique species surrounding each Fox
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);

    int score = 0;
    var foxGroups = player.getBoard().findAnimalGroups(Animal.FOX);

    for (var group : foxGroups) {
      for (var tile : group) {
        var neighbors = player.getBoard().getNeighbors(tile);
        var uniqueSpecies = new HashSet<Animal>();

        // Collect unique species from neighboring tiles
        for (var neighbor : neighbors) {
          if (neighbor.isTileAnimalPair()) { // Ensure the neighbor is a TileAnimalPair
            uniqueSpecies.add(neighbor.asTileAnimalPair().animal());
          }
        }

        // Add points for the unique species, up to a maximum of 5 points per Fox
        score += Math.min(uniqueSpecies.size(), 5);
      }
    }

    return score;
  }
}
