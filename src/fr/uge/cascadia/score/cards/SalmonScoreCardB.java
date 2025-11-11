package fr.uge.cascadia.score.cards;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Represents the scoring rule for Salmon groups with a maximum size limit.
 *
 * <p>This rule awards points as follows:
 * - 1 Salmon in a group: 2 points.
 * - 2 Salmons in a group: 4 points.
 * - 3 Salmons in a group: 9 points.
 * - 4 Salmons in a group: 11 points.
 * - 5 Salmons in a group: 17 points.
 * 
 * Groups larger than 5 Salmons do not earn additional points.</p>
 */
public record SalmonScoreCardB() implements ScoreRule {

  /**
   * Calculates the score for a player based on the size of Salmon groups,
   * with a maximum score for groups of 5 Salmons.
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
      else if (size == 2) score += 4; // 2 Salmons in a group: 4 points
      else if (size == 3) score += 9; // 3 Salmons in a group: 9 points
      else if (size == 4) score += 11; // 4 Salmons in a group: 11 points
      else if (size == 5) score += 17; // 5 Salmons in a group: 17 points
      // Groups larger than 5 do not earn additional points
    }

    return score;
  }
}
