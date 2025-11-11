package fr.uge.cascadia.score;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.score.cards.BuzzardScoreCardA;
import fr.uge.cascadia.score.cards.BuzzardScoreCardB;
import fr.uge.cascadia.score.cards.BuzzardScoreCardC;
import fr.uge.cascadia.score.cards.BuzzardScoreCardD;
import fr.uge.cascadia.score.cards.ScoreRule;

/**
 * Represents the scoring mechanism for the Buzzard species in the Cascadia game.
 *
 * <p>The scoring logic calculates points for the player based on groups of Buzzards
 * on their game board. The actual scoring logic is to be implemented within the
 * {@code calculateScore} method.
 */
public record BuzzardScore(ScoreRule selectedRule) implements ScoringRule {

  /**
   * Calculates the score for the given player based on groups of Buzzards.
   *
   * <p>The current implementation serves as a placeholder for the scoring logic
   * specific to the Buzzard species.
   *
   * @param player the player for whom the score is being calculated
   * @return the total score for the Buzzard groups on the player's board
   * @throws NullPointerException if {@code player} is {@code null}
   */
	public BuzzardScore {
    Objects.requireNonNull(selectedRule);
  }

  /**
   * Constructs a {@code BearScore} with a randomly selected scoring rule.
   */
  public BuzzardScore(String card) {
    this(selectRule(card));
  }

  /**
   * Calculates the score for the given player based on the selected bear scoring rule.
   *
   * @param player the player for whom the score is being calculated
   * @return the total score based on the selected rule
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);
    return selectedRule.calculateScore(player);
  }

  /**
   * Selects a random scoring rule for bears from the available options.
   *
   * @return a randomly selected {@code BearScoreRule}
   */
  private static ScoreRule selectRule(String card) {
  	Objects.requireNonNull(card);
    return switch (card.toUpperCase()) { 
      case "A" -> new BuzzardScoreCardA();
      case "B" -> new BuzzardScoreCardB();
      case "C" -> new BuzzardScoreCardC();
      case "D" -> new BuzzardScoreCardD();
      default -> throw new IllegalArgumentException("Invalid card: " + card);
    };
  }
}
