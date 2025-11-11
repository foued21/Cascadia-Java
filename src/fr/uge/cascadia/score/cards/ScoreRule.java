package fr.uge.cascadia.score.cards;

import fr.uge.cascadia.game.Player;

/**
 * Represents the scoring rule for an animal card in the Cascadia game.
 * 
 * <p>Each animal card in the game implements this interface to define its specific 
 * scoring logic. The scoring rule takes into account the player's current state, 
 * such as their board and other game factors, to calculate the score.</p>
 */
public interface ScoreRule {

    /**
     * Calculates the score for the given player based on the specific scoring rule
     * of the implementing animal card.
     * 
     * @param player the {@link Player} whose score is being calculated
     * @return the score as an integer value
     * @throws NullPointerException if the player is {@code null}
     */
    int calculateScore(Player player);
}
