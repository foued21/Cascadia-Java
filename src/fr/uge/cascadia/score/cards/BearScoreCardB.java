package fr.uge.cascadia.score.cards;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Represents the scoring rule for Bear groups specifically targeting groups of size 3.
 *
 * <p>This rule awards a fixed score of 10 points for each group of exactly 3 Bears in the environment.</p>
 */
public record BearScoreCardB() implements ScoreRule {

  /**
   * Calculates the score for a player based on the number of groups of exactly 3 Bears found in their environment.
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

    // award 10 points for each group of exactly 3 Bears
    for (var group : bearGroups) {
      if (group.size() == 3) {
        score += 10; // Fixed score of 10 points per group of 3 Bears
      }
    }

    return score;
  }
}
