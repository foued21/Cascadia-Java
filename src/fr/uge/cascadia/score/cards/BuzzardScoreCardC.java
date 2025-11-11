package fr.uge.cascadia.score.cards;

import java.util.Objects;

import fr.uge.cascadia.game.Environnement;
import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.TileAnimalPair;

/**
 * Represents the scoring rule for Buzzards based on lines of sight.
 *
 * <p>This rule awards 3 points for each line of sight between two Buzzards.
 * A single Buzzard can participate in multiple lines of sight.</p>
 */
public record BuzzardScoreCardC() implements ScoreRule {

  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);
    var board = player.getBoard();
    var buzzardGroups = board.findAnimalGroups(Animal.BUZZARD);

    int lineOfSightCount = 0;
    for (var group : buzzardGroups) {
      for (var buzzard : group) {
        lineOfSightCount += countLinesOfSight(board, buzzard);
      }
    }

    return lineOfSightCount * 3; // 3 points per line of sight
  }

  private int countLinesOfSight(Environnement board, TileAnimalPair buzzard) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(buzzard);

    int lineOfSightCount = 0;
    int[] startCoords = board.getCoordinates(buzzard);

    for (var direction : getDirections()) {
      if (checkLineOfSightInDirection(board, startCoords, direction)) {
        lineOfSightCount++;
      }
    }

    return lineOfSightCount;
}

	/**
	 * Returns the directions to explore for lines of sight.
	 *
	 * @return a 2D array of directions (dx, dy)
	 */
	private int[][] getDirections() {
    return new int[][] {
      { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 },
      { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
    };
	}
	
	/**
	 * Checks if there is a line of sight in the given direction.
	 *
	 * @param board the board to check
	 * @param startCoords the starting coordinates
	 * @param direction the direction to explore
	 * @return {@code true} if a line of sight is found, {@code false} otherwise
	 */
	private boolean checkLineOfSightInDirection(Environnement board, int[] startCoords, int[] direction) {
    int dx = direction[0];
    int dy = direction[1];
    int x = startCoords[0];
    int y = startCoords[1];

    while (true) {
      x += dx;
      y += dy;

      var lot = board.getTile(x, y);
      if (lot == null) {
        return false;
      }

      if (lot.isTileAnimalPair()) {
        var pair = lot.asTileAnimalPair();
        if (pair.animal().equals(Animal.BUZZARD)) {
            return true;
        } else {
          	return false;
        }
      }
    }
	}

}

