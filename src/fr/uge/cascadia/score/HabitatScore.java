package fr.uge.cascadia.score;

import java.util.Objects;
import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Habitat;

/**
 * Represents the scoring mechanism for habitat groups in the Cascadia game.
 * 
 * <p>This card awards points based on the size of the largest group of 
 * adjacent tiles of a specific habitat type on the player's board.
 * 
 * @param habitat the type of habitat for which the score will be calculated
 */
public record HabitatScore(Habitat habitat) implements ScoringRule {

  /**
   * Constructs a {@code HabitatScore} for the specified habitat type.
   *
   * @param habitat the type of habitat associated with this scoring card
   * @throws NullPointerException if {@code habitat} is {@code null}
   */
  public HabitatScore {
    Objects.requireNonNull(habitat);
  }

  /**
   * Calculates the score for the given player based on the size of the largest
   * group of adjacent tiles of the specified habitat type on their board.
   *
   * @param player the player for whom the score is being calculated
   * @return the size of the largest group of tiles of the specified habitat
   * @throws NullPointerException if {@code player} is {@code null}
   */
  @Override
  public int calculateScore(Player player) {
    Objects.requireNonNull(player);
    return player.getBoard().findLargestHabitatGroup(habitat);
  }
}
