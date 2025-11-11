package fr.uge.cascadia.score.cards;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Represents the scoring rule for Bears that awards points based on group sizes.
 *
 * <p>This rule awards:</p>
 * <ul>
 *   <li>5 points for each group of 2 Bears</li>
 *   <li>8 points for each group of 3 Bears</li>
 *   <li>13 points for each group of 4 or more Bears</li>
 * </ul>
 */
public record BearScoreCardD() implements ScoreRule {

  /**
   * Calculates the score for a player based on the sizes of Bear groups found in their environment.
   * Groups of sizes 2, 3, and 4 or more contribute different point values.
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

    for (var group : bearGroups) {
      int size = group.size();
      switch (size) {
        case 2:
          score += 5;
          break;
        case 3:
          score += 8;
          break;
        default:
          if (size >= 4) score += 13; // groupes de 4 ou plus
          break;
      }
    }

    return score;
  }
}
