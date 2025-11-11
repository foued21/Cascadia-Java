package fr.uge.cascadia.score;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.score.cards.ElkScoreCardA;
import fr.uge.cascadia.score.cards.ElkScoreCardB;
import fr.uge.cascadia.score.cards.ElkScoreCardC;
import fr.uge.cascadia.score.cards.ElkScoreCardD;
import fr.uge.cascadia.score.cards.ScoreRule;


/**
 * Represents the scoring mechanism for the Elk species in the Cascadia game.
 * 
 * <p>The score is calculated based on a randomly selected scoring rule
 * specific to the Elk. The rule is chosen from a predefined set of four
 * possible scoring rules.
 *
 * @param selectedRule the scoring rule to be applied for the Elk
 */
public record ElkScore(ScoreRule selectedRule) implements ScoringRule {

  /**
   * Constructs an {@code ElkScore} with a specific scoring rule.
   *
   * @param selectedRule the scoring rule to be applied for the Elk
   * @throws NullPointerException if {@code selectedRule} is {@code null}
   */
  public ElkScore {
    Objects.requireNonNull(selectedRule);
  }

  /**
   * Constructs an {@code ElkScore} with a randomly selected scoring rule.
   */
  public ElkScore(String card) {
    this(selectRule(card));
  }

  /**
   * Selects a random scoring rule for the Elk from the predefined set of rules.
   *
   * @return a randomly selected {@code ElkScoreRule}
   */
  private static ScoreRule selectRule(String card) {
  	Objects.requireNonNull(card);
  	return switch (card.toUpperCase()) {
    case "A" -> new ElkScoreCardA();
    case "B" -> new ElkScoreCardB();
    case "C" -> new ElkScoreCardC();
    case "D" -> new ElkScoreCardD();
    default -> throw new IllegalArgumentException("Invalid card: " + card);
    };
  }

  /**
   * Calculates the score for the given player based on the selected Elk scoring rule.
   *
   * @param player the player for whom the score is being calculated
   * @return the total score for the Elk based on the selected rule
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);
    return selectedRule.calculateScore(player);
  }
}
