package fr.uge.cascadia.score.cards;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Represents the scoring rule for Bears that awards points based on group sizes
 * and provides a bonus for having groups of all specified sizes.
 *
 * <p>This rule awards:</p>
 * <ul>
 *   <li>2 points for each group of 1 Bear</li>
 *   <li>5 points for each group of 2 Bears</li>
 *   <li>8 points for each group of 3 Bears</li>
 * </ul>
 * <p>If at least one group of each size (1, 2, and 3) exists, a bonus of 3 points is awarded.</p>
 */
public record BearScoreCardC() implements ScoreRule {

  /**
   * Calculates the score for a player based on the sizes of Bear groups found in their environment.
   * Groups of sizes 1, 2, and 3 contribute different point values, and a bonus is awarded
   * if at least one group of each size exists.
   *
   * @param player the player for whom the score is being calculated
   * @return the total score for the Bears according to this scoring rule
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);
    int score = 0;

    var bearGroups = player.getBoard().findAnimalGroups(Animal.BEAR);
    boolean hasOne = false, hasTwo = false, hasThree = false;

    for (var group : bearGroups) {
      int size = group.size();
      switch (size) {
        case 1:
          score += 2;
          hasOne = true;
          break;
        case 2:
          score += 5;
          hasTwo = true;
          break;
        case 3:
          score += 8;
          hasThree = true;
          break;
        default:
          break;
      }
    }

    // Bonus for having at least one group of each size (1, 2, and 3)
    if (hasOne && hasTwo && hasThree) {
      score += 3;
    }

    return score;
  }
}
