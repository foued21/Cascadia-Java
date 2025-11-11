package fr.uge.cascadia.score;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Animal;
import fr.uge.cascadia.structure.FauneBag;
import fr.uge.cascadia.structure.Habitat;

/**
 * Manages score calculations for a specific player using Java Streams.
 */
public class ScoreCalculator {
	private final Player player;

	/**
	 * Constructs a ScoreCalculator for the given player.
	 *
	 * @param player the player whose score will be calculated
	 */
	public ScoreCalculator(Player player) {
		this.player = Objects.requireNonNull(player, "Player cannot be null");
	}

	/**
	 * Calculates the score for habitats.
	 *
	 * @return a map of habitats to their scores
	 */
	public Map<Habitat, Integer> calculateTilesScore() {

		return Habitat.allHabitats().stream()
				.collect(Collectors.toMap(Function.identity(), habitat -> new HabitatScore(habitat).calculateScore(player)));
	}

	/**
	 * Calculates the score for animals based on family scoring.
	 *
	 * @param animals the FauneBag containing all animals in the game
	 * @return a map of animals to their scores
	 */
	private Map<Animal, Integer> calculateFamilyScore(FauneBag animals) {
		Objects.requireNonNull(animals);
		return animals.animalList().stream()
				.collect(Collectors.toMap(Function.identity(), animal -> new FamilyScoreCard(animal).calculateScore(player)));
	}

	/**
	 * Calculates the score for animals based on intermediate scoring.
	 *
	 * @param animals the FauneBag containing all animals in the game
	 * @return a map of animals to their scores
	 */
	private Map<Animal, Integer> calculateIntermediateScore(FauneBag animals) {
		Objects.requireNonNull(animals);
		return animals.animalList().stream().collect(
				Collectors.toMap(Function.identity(), animal -> new IntermediateScoreCard(animal).calculateScore(player)));
	}

	/**
	 * Calculates the score for animals using a specific scoring card.
	 *
	 * @param animals the FauneBag containing all animals in the game
	 * @param card    the scoring card identifier
	 * @return a map of animals to their scores
	 */
	private Map<Animal, Integer> calculateScoreWithCard(FauneBag animals, String card) {
		Objects.requireNonNull(animals);
		Objects.requireNonNull(card);

		return animals.animalList().stream().collect(Collectors.toMap(Function.identity(), animal -> {
			ScoringRule scoringRule = switch (animal) {
			case BEAR -> new BearScore(card);
			case SALMON -> new SalmonScore(card);
			case FOX -> new FoxScore(card);
			case ELK -> new ElkScore(card);
			case BUZZARD -> new BuzzardScore(card);
			default -> throw new IllegalStateException("Unexpected animal: " + animal);
			};
			return scoringRule.calculateScore(player);
		}));
	}

	
	/**
	 * Calculates the player's animal scores based on the chosen scoring method.
	 *
	 * @param choice  the scoring method to use:
	 *                <ul>
	 *                  <li>1: Family scoring</li>
	 *                  <li>2: Intermediate scoring</li>
	 *                  <li>3: Scoring with a specific card</li>
	 *                </ul>
	 * @param card    the identifier of the scoring card (used when {@code choice} is 3)
	 * @param animals the {@link FauneBag} containing all animals in the game
	 * @return a map of {@link Animal} to their respective scores
	 * @throws NullPointerException     if {@code animals} or {@code card} is {@code null}
	 * @throws IllegalArgumentException if {@code choice} is not 1, 2, or 3
	 */
	public Map<Animal, Integer> calculateAnimalsScore(int choice, String card, FauneBag animals) {
		Objects.requireNonNull(animals);
		Objects.requireNonNull(card);
		switch (choice) {
		case 1:
			return calculateFamilyScore(animals);
		case 2:
			return calculateIntermediateScore(animals);
		case 3:
			return calculateScoreWithCard(animals, card);
		}

		throw new IllegalArgumentException();
	}

	/**
	 * Calculates the total score by summing animal and habitat scores.
	 *
	 * @param animalScores  the animal scores map
	 * @param habitatScores the habitat scores map
	 * @return the total score
	 */
	public int calculateTotalScore(Map<Animal, Integer> animalScores, Map<Habitat, Integer> habitatScores,
			Map<Habitat, Integer> bonuses, int nb_tokens) {
		Objects.requireNonNull(animalScores);
		Objects.requireNonNull(habitatScores);

		int totalAnimalScore = animalScores.values().stream().mapToInt(Integer::intValue).sum();
		int totalHabitatScore = habitatScores.values().stream().mapToInt(Integer::intValue).sum();
		int totalBonusScore = bonuses.values().stream().mapToInt(Integer::intValue).sum();

		return totalAnimalScore + totalHabitatScore + totalBonusScore + nb_tokens;
	}
}
