package fr.uge.cascadia.score.cards;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Represents the scoring rule for Elk based on the {@code ElkScoreCardA}.
 *
 * <p>This scoring card rewards points based on the size of Elk groups found
 * in the player's environment. The scoring is as follows:</p>
 * <ul>
 *   <li>1 Elk: 2 points</li>
 *   <li>2 Elk: 5 points</li>
 *   <li>3 Elk: 9 points</li>
 *   <li>4 Elk: 13 points</li>
 * </ul>
 *
 * <p>Groups larger than 4 Elk do not provide additional points.</p>
 */
public record ElkScoreCardA() implements ScoreRule {

  /**
   * Calculates the score for a given player based on the {@code ElkScoreCardA} rule.
   *
   * <p>The method computes the total score by evaluating groups of Elk
   * in the player's environment. Each group contributes points according to
   * the predefined scoring criteria.</p>
   *
   * @param player the player for whom the score is being calculated
   * @return the total score calculated according to the rule
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);

    int score = 0;
    var elkGroups = player.getBoard().findAnimalGroups(Animal.ELK);

    for (var group : elkGroups) {
      int size = group.size();
      if (size == 1) score += 2;
      else if (size == 2) score += 5;
      else if (size == 3) score += 9;
      else if (size == 4) score += 13;
    }
    return score;
  }
}
