package fr.uge.cascadia.score.cards;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import fr.uge.cascadia.game.Environnement;
import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.TileAnimalPair;

/**
 * Represents the scoring rule for Elk based on the {@code ElkScoreCardD}.
 *
 * <p>This scoring card rewards points for groups of Elk that form a closed circle
 * in the player's environment.</p>
 *
 * <p>Scoring criteria:</p>
 * <ul>
 *   <li>1 Elk: 2 points</li>
 *   <li>2 Elk: 5 points</li>
 *   <li>3 Elk: 8 points</li>
 *   <li>4 Elk: 12 points</li>
 *   <li>5 Elk: 16 points</li>
 *   <li>6 Elk: 21 points</li>
 * </ul>
 * 
 * <p>Groups that do not form a closed circle do not contribute to the score.</p>
 */
public record ElkScoreCardD() implements ScoreRule {

  /**
   * Calculates the score for a given player based on the {@code ElkScoreCardD} rule.
   *
   * <p>The method identifies all Elk groups in the player's environment, checks whether
   * each group forms a closed circle, and calculates the score accordingly.</p>
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
        if (isCircle(group, player.getBoard())) {
          int size = group.size();
          if (size == 1) score += 2;
          else if (size == 2) score += 5;
          else if (size == 3) score += 8;
          else if (size == 4) score += 12;
          else if (size == 5) score += 16;
          else if (size == 6) score += 21;
        }
      }

      return score;
  }

  /**
   * Determines if a group of Elk forms a closed circle.
   *
   * <p>A group is considered a closed circle if all Elk are connected and form
   * a continuous loop, returning to the starting Elk.</p>
   *
   * @param group the group of Elk to check
   * @param environment the game environment containing the tiles
   * @return {@code true} if the group forms a closed circle, {@code false} otherwise
   * @throws NullPointerException if {@code group} or {@code environment} is {@code null}
   */
  private boolean isCircle(List<TileAnimalPair> group, Environnement environment) {
    if (group.size() < 3) return false;

    var visited = new HashSet<TileAnimalPair>();
    var start = group.get(0);

    return dfsForCircle(start, null, group, visited, environment) && visited.size() == group.size();
  }

  /**
   * Performs a depth-first search (DFS) to determine if a group forms a closed circle.
   *
   * <p>The method recursively visits neighbors of the current Elk, keeping track of visited Elk
   * and ensuring that the group forms a loop.</p>
   *
   * @param current the current {@code TileAnimalPair} being visited
   * @param parent the previous {@code TileAnimalPair} visited in the DFS
   * @param group the group of Elk being checked
   * @param visited the set of Elk that have already been visited
   * @param environment the game environment containing the tiles
   * @return {@code true} if the group forms a closed circle, {@code false} otherwise
   * @throws NullPointerException if any argument is {@code null}
   */
  private boolean dfsForCircle(
      TileAnimalPair current,
      TileAnimalPair parent,
      List<TileAnimalPair> group,
      Set<TileAnimalPair> visited,
      Environnement environment) {

    if (visited.contains(current)) {
      // Return to the starting point
      return current.equals(group.get(0));
    }

    visited.add(current);

    // Explore neighbors
    for (var neighbor : environment.getNeighbors(current)) {
      if (neighbor.isTileAnimalPair()) { // Ensure the neighbor is a TileAnimalPair
        var neighborPair = neighbor.asTileAnimalPair();

        if (!neighborPair.equals(parent) && group.contains(neighborPair)) {
          if (dfsForCircle(neighborPair, current, group, visited, environment)) {
            return true;
          }
        }
      }
    }

    return false;
  }
}
