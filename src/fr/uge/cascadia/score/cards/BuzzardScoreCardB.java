package fr.uge.cascadia.score.cards;

import java.util.List;
import java.util.Objects;

import fr.uge.cascadia.game.Environnement;
import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.Lot;
import fr.uge.cascadia.structure.TileAnimalPair;

/**
 * Represents the scoring rule for Buzzards based on isolated buzzards with at least one other in line of sight.
 *
 * <p>This rule awards points for Buzzards isolated but having another Buzzard in line of sight:
 * - 2 Buzzards: 5 points.
 * - 3 Buzzards: 9 points.
 * - 4 Buzzards: 12 points.
 * - 5 Buzzards: 16 points.
 * - 6 Buzzards: 21 points.
 * - 7 Buzzards: 24 points.
 * - 8 or more Buzzards: 26 points.</p>
 */
public record BuzzardScoreCardB() implements ScoreRule {

  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);

    var board = player.getBoard();
    var buzzardGroups = board.findAnimalGroups(Animal.BUZZARD);
    int countWithLineOfSight = 0;

    for (var group : buzzardGroups) {
      if (group.size() == 1) { // Isolated buzzard
        var isolatedBuzzard = group.get(0);
        if (hasLineOfSight(board, isolatedBuzzard, buzzardGroups)) {
          countWithLineOfSight++;
        }
      }
    }

    // Award points based on the number of Buzzards with line of sight
    return switch (countWithLineOfSight) {
      case 2 -> 5;
      case 3 -> 9;
      case 4 -> 12;
      case 5 -> 16;
      case 6 -> 21;
      case 7 -> 24;
      default -> countWithLineOfSight >= 8 ? 26 : 0;
    };
  }

  /**
   * Checks if the given Buzzard has another Buzzard in its line of sight.
   *
   * @param board the player's board to check
   * @param buzzard the buzzard to check
   * @param buzzardGroups the precomputed list of all buzzard groups
   * @return {@code true} if the buzzard has another in line of sight, {@code false} otherwise
   */
  private boolean hasLineOfSight(Environnement board, TileAnimalPair buzzard, 
                                 List<List<TileAnimalPair>> buzzardGroups) {
    int[] startCoords = board.getCoordinates(buzzard);

    for (var group : buzzardGroups) {
      for (var otherBuzzard : group) {
        if (!otherBuzzard.equals(buzzard)) { // Exclude itself
          int[] otherCoords = board.getCoordinates(otherBuzzard);

          if (isInLineOfSight(board, startCoords, otherCoords)) {
            return true;
          }
        }
      }
    }

    return false; 
  }

  /**
   * Checks if two Buzzards are in line of sight.
   *
   * @param board the board to check
   * @param start the starting coordinates of the first buzzard
   * @param end the ending coordinates of the second buzzard
   * @return {@code true} if there is a line of sight, {@code false} otherwise
   */
  private boolean isInLineOfSight(Environnement board, int[] start, int[] end) {
    int dx = Integer.signum(end[0] - start[0]); // Direction in x
    int dy = Integer.signum(end[1] - start[1]); // Direction in y

    int x = start[0];
    int y = start[1];

    while (x != end[0] || y != end[1]) {
      x += dx;
      y += dy;

      Lot lot = board.getTile(x, y);
      if (lot == null) return false;
      if (lot.isTileAnimalPair() && !lot.asTileAnimalPair().animal().equals(Animal.BUZZARD)) {
        return false; // Obstacle encountered
      }
    }

    return true;
  }
}
