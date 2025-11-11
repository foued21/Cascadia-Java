package fr.uge.cascadia.score;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.score.cards.ScoreRule;

import fr.uge.cascadia.score.cards.SalmonScoreCardA;
import fr.uge.cascadia.score.cards.SalmonScoreCardB;
import fr.uge.cascadia.score.cards.SalmonScoreCardC;
import fr.uge.cascadia.score.cards.SalmonScoreCardD;


/**
 * Represents the scoring mechanism for salmon in the Cascadia game.
 * 
 * <p>A specific rule is selected to determine the score for salmon
 * based on one of the predefined scoring cards:
 * <ul>
 *   <li>{@link SalmonScoreCardA}</li>
 *   <li>{@link SalmonScoreCardB}</li>
 *   <li>{@link SalmonScoreCardC}</li>
 *   <li>{@link SalmonScoreCardD}</li>
 * </ul>
 * 
 * <p>A random rule is selected if no specific rule is provided.
 * The score is calculated based on the selected rule and the player's board.
 * 
 * @see SalmonScoreRule
 * @see SalmonScoreCardA
 * @see SalmonScoreCardB
 * @see SalmonScoreCardC
 * @see SalmonScoreCardD
 */
public record SalmonScore(ScoreRule selectedRule) implements ScoringRule {

  /**
   * Constructs a {@code SalmonScore} with the specified rule.
   *
   * @param selectedRule the scoring rule to be used
   * @throws NullPointerException if {@code selectedRule} is {@code null}
   */
  public SalmonScore {
    Objects.requireNonNull(selectedRule);
  }

  /**
   * Constructs a {@code SalmonScore} with a randomly selected rule.
   */
  public SalmonScore(String card) {
    this(selectRule(card));
  }

  /**
   * Randomly selects a salmon scoring rule from the available options.
   *
   * @return a randomly selected {@link SalmonScoreRule}
   */
  private static ScoreRule selectRule(String card) {
  	Objects.requireNonNull(card);
  	 return switch (card.toUpperCase()) { 
     case "A" -> new SalmonScoreCardA();
     case "B" -> new SalmonScoreCardB();
     case "C" -> new SalmonScoreCardC();
     case "D" -> new SalmonScoreCardD();
     default -> throw new IllegalArgumentException("Invalid card: " + card);
   };
  }

  /**
   * Calculates the salmon score for the given player using the selected rule.
   *
   * @param player the player for whom the score is being calculated
   * @return the calculated salmon score
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);
    return selectedRule.calculateScore(player);
  }
}
