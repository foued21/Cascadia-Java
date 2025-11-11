package fr.uge.cascadia.score.cards;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import fr.uge.cascadia.game.Environnement;
import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.TileAnimalPair;

/**
 * Represents the scoring rule for Buzzards based on pairs in straight lines.
 *
 * <p>This rule awards points based on the number of different animals separating each pair of Buzzards:
 * - 1 animal: 4 points.
 * - 2 animals: 7 points.
 * - 3 or more animals: 9 points.</p>
 */
public record BuzzardScoreCardD() implements ScoreRule {

  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);
    var board = player.getBoard();
    var buzzardGroups = board.findAnimalGroups(Animal.BUZZARD);

    int totalScore = 0;
    for (var group : buzzardGroups) {
      totalScore += calculateGroupScore(board, group);
    }

    return totalScore;
  }

  /**
   * Calculates the score for all pairs in a group of Buzzards.
   *
   * @param board the board to check
   * @param group the group of Buzzards
   * @return the total score for the group
   */
  private int calculateGroupScore(Environnement board, List<TileAnimalPair> group) {
    int groupScore = 0;

    for (int i = 0; i < group.size(); i++) {
      for (int j = i + 1; j < group.size(); j++) {
        groupScore += calculatePairScore(board, group.get(i), group.get(j));
      }
    }

    return groupScore;
  }

  /**
   * Calculates the score for a pair of Buzzards based on the animals between them.
   *
   * @param board the board to check
   * @param buzzard1 the first Buzzard in the pair
   * @param buzzard2 the second Buzzard in the pair
   * @return the score for the pair
   */
  private int calculatePairScore(Environnement board, TileAnimalPair buzzard1, TileAnimalPair buzzard2) {
    if (!isAligned(board.getCoordinates(buzzard1), board.getCoordinates(buzzard2))) {
      return 0;
    }

    int uniqueAnimalCount = countUniqueAnimalsBetween(board, buzzard1, buzzard2);
    return getScoreForAnimalCount(uniqueAnimalCount);
  }

  /**
   * Counts the number of unique animals between two Buzzards.
   *
   * @param board the board to check
   * @param buzzard1 the first Buzzard
   * @param buzzard2 the second Buzzard
   * @return the count of unique animals
   */
  private int countUniqueAnimalsBetween(Environnement board, TileAnimalPair buzzard1, TileAnimalPair buzzard2) {
    var uniqueAnimals = new HashSet<Animal>();
    int[] start = board.getCoordinates(buzzard1);
    int[] end = board.getCoordinates(buzzard2);

    int dx = Integer.signum(end[0] - start[0]);
    int dy = Integer.signum(end[1] - start[1]);
    int x = start[0] + dx;
    int y = start[1] + dy;

    while (x != end[0] || y != end[1]) {
      var lot = board.getTile(x, y);
      if (lot != null && lot.isTileAnimalPair()) {
        var animal = lot.asTileAnimalPair().animal();
        if (!animal.equals(Animal.BUZZARD)) {
          uniqueAnimals.add(animal);
        }
      }

      x += dx;
      y += dy;
    }

    return uniqueAnimals.size();
  }

  /**
   * Determines the score based on the number of unique animals.
   *
   * @param uniqueAnimalCount the number of unique animals
   * @return the score for the pair
   */
  private int getScoreForAnimalCount(int uniqueAnimalCount) {
    return switch (uniqueAnimalCount) {
      case 1 -> 4;  // 1 animal → 4 points
      case 2 -> 7;  // 2 animals → 7 points
      default -> 9; // 3 or more animals → 9 points
    };
  }

  /**
   * Checks if two Buzzards are aligned (in a straight line).
   *
   * @param start the starting coordinates
   * @param end the ending coordinates
   * @return {@code true} if the Buzzards are aligned, {@code false} otherwise
   */
  private boolean isAligned(int[] start, int[] end) {
    int dx = Math.abs(end[0] - start[0]);
    int dy = Math.abs(end[1] - start[1]);

    return dx == 0 || dy == 0 || dx == dy; // Aligned if same row, column, or diagonal
  }
}
