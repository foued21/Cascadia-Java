package fr.uge.cascadia.score.cards;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Represents the scoring rule for Salmon groups with incremental scoring based on group size.
 *
 * <p>This rule awards points for Salmon groups as follows:
 * - 1 Salmon in a group: 2 points.
 * - 2 Salmons in a group: 5 points.
 * - 3 Salmons in a group: 8 points.
 * - 4 Salmons in a group: 12 points.
 * - 5 Salmons in a group: 16 points.
 * - 6 Salmons in a group: 20 points.
 * - 7 or more Salmons in a group: 25 points.</p>
 */
public record SalmonScoreCardA() implements ScoreRule {

  /**
   * Calculates the score for a player based on the size of Salmon groups,
   * with incremental scoring for groups of varying sizes.
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
      if (size == 1) score += 2;    // 1 Salmon in a group: 2 points
      else if (size == 2) score += 5; // 2 Salmons in a group: 5 points
      else if (size == 3) score += 8; // 3 Salmons in a group: 8 points
      else if (size == 4) score += 12; // 4 Salmons in a group: 12 points
      else if (size == 5) score += 16; // 5 Salmons in a group: 16 points
      else if (size == 6) score += 20; // 6 Salmons in a group: 20 points
      else if (size >= 7) score += 25; // 7 or more Salmons in a group: 25 points
    }

    return score;
  }
}
