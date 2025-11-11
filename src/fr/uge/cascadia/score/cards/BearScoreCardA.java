package fr.uge.cascadia.score.cards;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Represents the scoring rule for Bear groups based on the number of pairs of bears.
 *
 * <p>This rule awards points for Bear pairs as follows:
 * - 1 pair of Bears: 4 points.
 * - 2 pairs of Bears: 11 points.
 * - 3 pairs of Bears: 19 points.
 * - 4 or more pairs of Bears: 27 points.</p>
 */
public record BearScoreCardA() implements ScoreRule {

  /**
   * Calculates the score for a player based on the number of pairs of Bears found in their environment.
   * Each pair of Bears contributes to the score, with incremental rewards for multiple pairs.
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
    int pairCount = 0;

    // Calculate the number of pairs across all Bear groups
    for (var group : bearGroups) {
      pairCount += group.size() / 2; // Each 2 Bears form one pair
    }

    // Award points based on the number of pairs
    if (pairCount == 1) score += 4;    // 1 pair: 4 points
    else if (pairCount == 2) score += 11; // 2 pairs: 11 points
    else if (pairCount == 3) score += 19; // 3 pairs: 19 points
    else if (pairCount >= 4) score += 27; // 4+ pairs: 27 points

    return score;
  }
}
