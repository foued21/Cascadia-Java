package fr.uge.cascadia.score;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.score.cards.FoxScoreCardA;
import fr.uge.cascadia.score.cards.FoxScoreCardB;
import fr.uge.cascadia.score.cards.FoxScoreCardC;
import fr.uge.cascadia.score.cards.FoxScoreCardD;
import fr.uge.cascadia.score.cards.ScoreRule;

/**
 * Represents the scoring mechanism for the fox animal in the Cascadia game.
 * 
 * <p>This card selects one of four predefined scoring rules for the fox
 * and calculates the player's score based on the selected rule.
 *
 * @param selectedRule the specific scoring rule to be applied for the fox
 */
public record FoxScore(ScoreRule selectedRule) implements ScoringRule {

  /**
   * Constructs a {@code FoxScore} with the specified scoring rule.
   *
   * @param selectedRule the specific rule used to calculate the fox's score
   * @throws NullPointerException if {@code selectedRule} is {@code null}
   */
  public FoxScore {
    Objects.requireNonNull(selectedRule);
  }

  /**
   * Constructs a {@code FoxScore} with a randomly selected scoring rule.
   */
  public FoxScore(String card) {
    this(selectRule(card));
  }

  /**
   * Selects a random fox scoring rule from the available set of rules.
   *
   * @return a randomly chosen {@code FoxScoreRule}
   */
  private static ScoreRule selectRule(String card) {
  	Objects.requireNonNull(card);
  	 return switch (card.toUpperCase()) {
     case "A" -> new FoxScoreCardA();
     case "B" -> new FoxScoreCardB();
     case "C" -> new FoxScoreCardC();
     case "D" -> new FoxScoreCardD();
     default -> throw new IllegalArgumentException("Invalid card: " + card);
   };
  }

  /**
   * Calculates the score for the given player using the selected fox scoring rule.
   *
   * @param player the player for whom the score is being calculated
   * @return the calculated score based on the selected rule
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
  	Objects.requireNonNull(player);
    return selectedRule.calculateScore(player);
  }
}
