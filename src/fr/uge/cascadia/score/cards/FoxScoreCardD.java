package fr.uge.cascadia.score.cards;

import java.util.ArrayList;
import java.util.Objects;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;

/**
 * Implements the scoring rule for Foxes according to the "D" card in the Cascadia game.
 * 
 * <p>This rule calculates points based on the number of distinct species pairs 
 * surrounding each pair of Foxes on the board.</p>
 * 
 * <p>Rules:</p>
 * <ul>
 *   <li>1 pair of species: +5 points</li>
 *   <li>2 pairs of species: +7 points</li>
 *   <li>3 pairs of species: +9 points</li>
 *   <li>4 or more pairs of species: +11 points</li>
 * </ul>
 * 
 */
public record FoxScoreCardD() implements ScoreRule {

  /**
   * Calculates the score for the player based on the "D" Fox scoring rule.
   *
   * <p>Pairs of Foxes are identified, and the distinct species surrounding them 
   * (excluding other Foxes) are counted to determine the score.</p>
   *
   * @param player the player whose score is being calculated
   * @return the calculated score based on the number of species pairs around each Fox pair
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);

    int score = 0;
    var foxGroups = player.getBoard().findAnimalGroups(Animal.FOX);

    // Filter only groups that are pairs of Foxes
    var foxPairs = foxGroups.stream()
      .filter(group -> group.size() == 2)
      .toList();

    for (var pair : foxPairs) {
      var species = new ArrayList<Animal>();

      // Gather all distinct species around the Fox pair
      for (var foxTile : pair) {
        var neighbors = player.getBoard().getNeighbors(foxTile);
        for (var neighbor : neighbors) {
          if (neighbor.isTileAnimalPair()) { // Ensure neighbor is a TileAnimalPair
            var neighborAnimal = neighbor.asTileAnimalPair().animal();
            if (!neighborAnimal.equals(Animal.FOX) && !species.contains(neighborAnimal)) {
              species.add(neighborAnimal);
            }
          }
        }
      }

      // Calculate the total number of species pairs
      int totalPairs = species.size() / 2;

      // Assign points based on the total number of pairs
      if (totalPairs == 1) score += 5;
      else if (totalPairs == 2) score += 7;
      else if (totalPairs == 3) score += 9;
      else if (totalPairs >= 4) score += 11;
    }

    return score;
  }

}
