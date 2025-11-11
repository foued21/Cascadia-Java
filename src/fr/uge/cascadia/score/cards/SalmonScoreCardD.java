package fr.uge.cascadia.score.cards;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import fr.uge.cascadia.game.Environnement;
import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.TileAnimalPair;

/**
 * Represents the scoring rule for Salmon based on their groups and adjacent animals.
 *
 * <p>This rule awards points as follows:
 * - 1 point per Salmon in a group (banc).
 * - 1 additional point for each animal adjacent to the group.</p>
 */
public record SalmonScoreCardD() implements ScoreRule {

  /**
   * Calculates the score for a player based on the Salmon groups and their adjacent animals.
   *
   * @param player the player for whom the score is being calculated
   * @return the total score for the Salmon according to this scoring rule
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);

    int score = 0;
    var salmonGroups = player.getBoard().findAnimalGroups(Animal.SALMON);

    for (var group : salmonGroups) {
      if (group.size() >= 3) {
        score += group.size(); // 1 point per Salmon in the group
        score += countAdjacentAnimals(group, player.getBoard()); // 1 point per adjacent animal
      }
    }

    return score;
  }

  /**
   * Counts the number of unique animals adjacent to a group of Salmon tiles.
   *
   * @param group the group of Salmon tiles
   * @param environment the game environment containing the tiles
   * @return the number of unique animals adjacent to the group
   * @throws NullPointerException if {@code group} or {@code environment} is {@code null}
   */
  private int countAdjacentAnimals(List<TileAnimalPair> group, Environnement environment) {
    Objects.requireNonNull(group);
    Objects.requireNonNull(environment);

    var adjacentAnimals = new HashSet<Animal>();

    for (var tile : group) {
      var neighbors = environment.getNeighbors(tile);
      for (var neighbor : neighbors) {
        adjacentAnimals.addAll(Arrays.asList(neighbor.asTileAnimalPair().animal())); // add adjacent animals
      }
    }

    return adjacentAnimals.size();
  }
}
