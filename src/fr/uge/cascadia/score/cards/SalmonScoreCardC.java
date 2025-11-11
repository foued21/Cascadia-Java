package fr.uge.cascadia.score.cards;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Represents the scoring rule for Salmon groups based on their size.
 *
 * <p>This rule awards points as follows:
 * - 3 Salmons in a group: 10 points.
 * - 4 Salmons in a group: 12 points.
 * - 5 or more Salmons in a group: 15 points.</p>
 */
public record SalmonScoreCardC() implements ScoreRule {

  /**
   * Calculates the score for a player based on the size of Salmon groups.
   *
   * @param player the player for whom the score is being calculated
   * @return the total score for the Salmon according to this scoring rule
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);

    int score = 0;
    var salmonGroups = player.getBoard().findAnimalGroups(Animal.SALMON);

    for (var group : salmonGroups) {
      int size = group.size();
      if (size == 3) score += 10;   // 3 Salmons in a group: 10 points
      else if (size == 4) score += 12; // 4 Salmons in a group: 12 points
      else if (size >= 5) score += 15; // 5 or more Salmons in a group: 15 points
    }

    return score;
  }
}
