package fr.uge.cascadia.score;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Habitat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerProgress {
    private final Player player;
    private final Path progressDirectory;

    /**
     * Constructs a PlayerProgress object for a given player.
     *
     * @param player            The player whose progress will be saved.
     * @param progressDirectory The directory where progress files will be stored.
     */
    public PlayerProgress(Player player, String progressDirectory) {
        this.player = Objects.requireNonNull(player, "Player cannot be null");
        this.progressDirectory = Path.of(progressDirectory);
    }

    /**
     * Saves the player's progress to a file.
     *
     * @param mode        The game mode (e.g., "hexagonal", "square", "terminal").
     * @param totalScore  The player's total score.
     * @param achievedIds The list of success IDs the player achieved.
     * @throws IOException if an error occurs during file writing.
     */
    public void saveProgress(String mode, int totalScore, List<Integer> achievedIds) throws IOException {
        Objects.requireNonNull(mode, "Mode cannot be null");
        Objects.requireNonNull(achievedIds, "Achieved IDs cannot be null");

        // Ensure the directory exists
        if (!Files.exists(progressDirectory)) {
            Files.createDirectories(progressDirectory);
        }

        // Create a filename based on player name and timestamp
        String filename = player.getName() + "_" + getCurrentTimestamp() + ".txt";
        Path progressFile = progressDirectory.resolve(filename);

        // Write player progress to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(progressFile.toFile()))) {
            writer.write("Player: " + player.getName());
            writer.newLine();
            writer.write("Mode: " + mode);
            writer.newLine();
            writer.write("Date: " + getCurrentTimestamp());
            writer.newLine();
            writer.write("Total Score: " + totalScore);
            writer.newLine();
            writer.write("Achievements: ");
            if (achievedIds.isEmpty()) {
                writer.write("None");
            } else {
                writer.write(achievedIds.toString());
            }
            writer.newLine();
        }
    }

    /**
     * Determines the player's achievements based on their scores and a list of all available successes.
     *
     * @param successManager The SuccessManager containing all possible successes.
     * @param totalScore     The player's total score.
     * @param tilesScore     The player's tile scores.
     * @param tokens         The player's remaining nature tokens.
     * @return A list of success IDs the player achieved.
     */
    public List<Integer> determineAchievements(SuccessManager successManager, int totalScore,
                                               Map<Habitat, Integer> tilesScore, int tokens) {
        Objects.requireNonNull(successManager, "SuccessManager cannot be null");
        Objects.requireNonNull(tilesScore, "TilesScore cannot be null");

        List<Integer> achievedIds = new java.util.ArrayList<>();

        // Example success checks (you can expand this as needed)
        if (totalScore >= 80) achievedIds.add(1);
        if (totalScore >= 85) achievedIds.add(2);
        if (totalScore >= 90) achievedIds.add(3);
        if (totalScore >= 95) achievedIds.add(4);
        if (totalScore >= 100) achievedIds.add(5);
        if (totalScore >= 105) achievedIds.add(6);
        if (totalScore >= 110) achievedIds.add(7);
        if (tokens == 0) achievedIds.add(8);

        // Specific habitat scoring achievements
        if (tilesScore.values().stream().anyMatch(score -> score >= 12)) achievedIds.add(17);
        if (tilesScore.values().stream().anyMatch(score -> score >= 15)) achievedIds.add(18);

        // Add additional achievement checks as needed here...

        return achievedIds;
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    }
}
