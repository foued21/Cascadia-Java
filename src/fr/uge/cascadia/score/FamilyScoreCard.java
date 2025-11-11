package fr.uge.cascadia.score;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Represents a scoring card for the family rule in the Cascadia game.
 * 
 * <p>This rule calculates the player's score based on the size of animal groups.
 * Smaller groups provide fewer points, while larger groups yield higher scores.
 *
 * @param animal the specific animal for which the score is being calculated
 */
public record FamilyScoreCard(Animal animal) implements ScoringRule {

  /**
   * Constructs a {@code FamilyScoreCard} for the specified animal.
   *
   * @param animal the animal for which the family score rule is applied
   * @throws NullPointerException if {@code animal} is {@code null}
   */
  public FamilyScoreCard {
    Objects.requireNonNull(animal);
  }

  /**
   * Calculates the score for the given player using the family rule.
   * 
   * <p>The score is determined as follows:
   * <ul>
   *   <li>1 animal in the group: 2 points</li>
   *   <li>2 animals in the group: 5 points</li>
   *   <li>3 or more animals in the group: 9 points</li>
   * </ul>
   *
   * @param player the player for whom the score is being calculated
   * @return the total score for the specified animal groups
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);

    int score = 0;
    var animalGroups = player.getBoard().findAnimalGroups(animal);

    for (var group : animalGroups) {
      int size = group.size();
      if (size == 1) score += 2;
      else if (size == 2) score += 5;
      else if (size >= 3) score += 9;
    }

    return score;
  }
}
