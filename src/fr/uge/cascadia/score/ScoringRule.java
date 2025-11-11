package fr.uge.cascadia.score;

import fr.uge.cascadia.game.Player;

/**
 * Represents a scoring rule for the Cascadia game.
 * Each implementation defines how to calculate the score
 * for a specific type of scoring rule or animal in the game.
 * 
 * <p>Classes implementing this interface should define the logic
 * for calculating scores based on the game's rules.
 * 
 * <p>Examples of scoring rules include:
 * <ul>
 *   <li>BearScore - Calculates score based on bear groups.</li>
 *   <li>BuzzardScore - Calculates score based on buzzard placement.</li>
 *   <li>ElkScore - Calculates score based on elk formations.</li>
 *   <li>FoxScore - Calculates score based on fox adjacency.</li>
 *   <li>SalmonScore - Calculates score for salmon chains.</li>
 *   <li>IntermediateScoreCard - Provides intermediate scoring rules.</li>
 *   <li>FamilyScoreCard - Provides scoring rules for families.</li>
 *   <li>HabitatScore - Calculates scores based on habitat groups.</li>
 * </ul>
 * 
 * @see BearScore
 * @see BuzzardScore
 * @see ElkScore
 * @see FoxScore
 * @see SalmonScore
 * @see IntermediateScoreCard
 * @see FamilyScoreCard
 * @see HabitatScore
 */
public sealed interface ScoringRule permits BearScore, BuzzardScore, ElkScore, FoxScore, SalmonScore, IntermediateScoreCard, FamilyScoreCard, HabitatScore {

  /**
   * Calculates the score for a specific player based on the implemented rule.
   *
   * @param player the player for whom the score is being calculated
   * @return the calculated score
   * @throws NullPointerException if the {@code player} is {@code null}
   */
  int calculateScore(Player player);
}
