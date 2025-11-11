package fr.uge.cascadia.score.cards;

import java.util.List;
import java.util.Objects;

import fr.uge.cascadia.game.Environnement;
import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.TileAnimalPair;

/**
 * Represents the scoring rule for Elk based on the {@code ElkScoreCardB}.
 *
 * <p>This scoring card rewards points based on the size of tightly packed Elk groups
 * in the player's environment. A group is considered tightly packed if each Elk has
 * at least two direct neighbors within the group.</p>
 * 
 * <p>Scoring criteria:</p>
 * <ul>
 *   <li>1 Elk: 2 points</li>
 *   <li>2 Elk: 5 points</li>
 *   <li>3 Elk: 9 points</li>
 *   <li>4 Elk: 13 points</li>
 * </ul>
 * 
 * <p>Groups that are not tightly packed do not contribute to the score.</p>
 */
public record ElkScoreCardB() implements ScoreRule {

  /**
   * Calculates the score for a given player based on the {@code ElkScoreCardB} rule.
   *
   * <p>The method identifies all Elk groups in the player's environment, checks whether
   * each group is tightly packed, and calculates the score accordingly.</p>
   *
   * @param player the player for whom the score is being calculated
   * @return the total score calculated according to the rule
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);

    int score = 0;
    var elkGroups = player.getBoard().findAnimalGroups(Animal.ELK);

    for (var group : elkGroups) {
      if (isTightlyPacked(group, player.getBoard())) {
        int size = group.size();
        if (size == 1) score += 2;
        else if (size == 2) score += 5;
        else if (size == 3) score += 9;
        else if (size == 4) score += 13;
      }
    }

    return score;
  }

  /**
   * Determines if a group of Elk is tightly packed.
   *
   * <p>A group is tightly packed if each Elk in the group has at least two direct
   * neighbors within the group.</p>
   *
   * @param group the group of Elk to check
   * @param environment the game environment containing the tiles
   * @return {@code true} if the group is tightly packed, {@code false} otherwise
   * @throws NullPointerException if {@code group} or {@code environment} is {@code null}
   */
  private boolean isTightlyPacked(List<TileAnimalPair> group, Environnement environment) {
    for (var tile : group) {
      int neighborCount = (int) environment.getNeighbors(tile).stream()
          .filter(group::contains) // Verifies that the neighbor belongs to the group
          .count();
      if (neighborCount < 2) return false; // Each tile must have at least 2 direct neighbors
    }
    return true;
  }
}
