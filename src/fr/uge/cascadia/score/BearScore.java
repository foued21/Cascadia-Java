package fr.uge.cascadia.score;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.score.cards.BearScoreCardA;
import fr.uge.cascadia.score.cards.BearScoreCardB;
import fr.uge.cascadia.score.cards.BearScoreCardC;
import fr.uge.cascadia.score.cards.BearScoreCardD;
import fr.uge.cascadia.score.cards.ScoreRule;


/**
 * Represents the scoring mechanism for the Bear species in the Cascadia game.
 *
 * <p>The scoring logic is determined by a specific rule (card) selected either
 * manually or randomly from the available rules.</p>
 */
public record BearScore(ScoreRule selectedRule) implements ScoringRule {

  /**
   * Constructs a {@code BearScore} with the specified scoring rule.
   *
   * @param selectedRule the scoring rule to be used for calculating the score
   * @throws NullPointerException if {@code selectedRule} is {@code null}
   */
  public BearScore {
    Objects.requireNonNull(selectedRule);
  }

  /**
   * Constructs a {@code BearScore} with a randomly selected scoring rule.
   */
  public BearScore(String card) {
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
      case "A" -> new BearScoreCardA();
      case "B" -> new BearScoreCardB();
      case "C" -> new BearScoreCardC();
      case "D" -> new BearScoreCardD();
      default -> throw new IllegalArgumentException("Invalid card: " + card);
    };
  }

}
