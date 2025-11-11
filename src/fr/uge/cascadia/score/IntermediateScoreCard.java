package fr.uge.cascadia.score;

import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Represents the scoring mechanism for the "Intermediate" scoring card
 * in the Cascadia game.
 * 
 * <p>This card awards points based on the size of groups of a specific animal
 * on the player's board:
 * <ul>
 *   <li>Groups of 2 animals: 5 points</li>
 *   <li>Groups of 3 animals: 8 points</li>
 *   <li>Groups of 4 or more animals: 12 points</li>
 * </ul>
 * 
 * @param animal the animal for which the score will be calculated
 */
public record IntermediateScoreCard(Animal animal) implements ScoringRule {

  /**
   * Constructs an {@code IntermediateScoreCard} for the specified animal.
   *
   * @param animal the animal associated with this scoring card
   * @throws NullPointerException if {@code animal} is {@code null}
   */
  public IntermediateScoreCard {
    Objects.requireNonNull(animal);
  }

  /**
   * Calculates the score for the given player based on the size of the groups
   * of the specified animal on their board.
   *
   * <p>The score is determined as follows:
   * <ul>
   *   <li>Groups of 2 animals: 5 points</li>
   *   <li>Groups of 3 animals: 8 points</li>
   *   <li>Groups of 4 or more animals: 12 points</li>
   * </ul>
   *
   * @param player the player for whom the score is being calculated
   * @return the total score for the specified animal
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);

    int score = 0;
    var animalGroups = player.getBoard().findAnimalGroups(animal);

    for (var group : animalGroups) {
      int size = group.size();
      if (size == 2) score += 5;
      else if (size == 3) score += 8;
      else if (size >= 4) score += 12;
    }

    return score;
  }
}
