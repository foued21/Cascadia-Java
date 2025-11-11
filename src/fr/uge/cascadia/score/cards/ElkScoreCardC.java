package fr.uge.cascadia.score.cards;

import java.util.List;
import java.util.Objects;

import fr.uge.cascadia.game.Environnement;
import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.TileAnimalPair;

/**
 * Represents the scoring rule for Elk based on the {@code ElkScoreCardC}.
 *
 * <p>This scoring card rewards points for groups of Elk that are arranged in a straight line
 * (either horizontally or vertically) in the player's environment.</p>
 * 
 * <p>Scoring criteria:</p>
 * <ul>
 *   <li>1 Elk: 2 points</li>
 *   <li>2 Elk: 5 points</li>
 *   <li>3 Elk: 9 points</li>
 *   <li>4 Elk: 13 points</li>
 * </ul>
 * 
 * <p>Groups that are not arranged in a straight line do not contribute to the score.</p>
 */
public record ElkScoreCardC() implements ScoreRule {

  /**
   * Calculates the score for a given player based on the {@code ElkScoreCardC} rule.
   *
   * <p>The method identifies all Elk groups in the player's environment, checks whether
   * each group forms a straight line, and calculates the score accordingly.</p>
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
        if (isStraightLine(group, player.getBoard())) {
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
   * Determines if a group of Elk is arranged in a straight line.
   *
   * <p>A group is considered to be in a straight line if all Elk in the group are
   * positioned either in the same row or in the same column.</p>
   *
   * @param group the group of Elk to check
   * @param environment the game environment containing the tiles
   * @return {@code true} if the group is in a straight line, {@code false} otherwise
   * @throws NullPointerException if {@code group} or {@code environment} is {@code null}
   */
  private boolean isStraightLine(List<TileAnimalPair> group, Environnement environment) {
    if (group.isEmpty()) return false;
    
    List<int[]> coordinates = group.stream()
                                   .map(tile -> environment.getCoordinates(tile))
                                   .toList();

    boolean sameRow = coordinates.stream().allMatch(coord -> coord[0] == coordinates.get(0)[0]);
    boolean sameColumn = coordinates.stream().allMatch(coord -> coord[1] == coordinates.get(0)[1]);

    return sameRow || sameColumn;
  }
}
