package fr.uge.cascadia.score;

import fr.uge.cascadia.game.Player;
import fr.uge.cascadia.structure.Habitat;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents the calculation and assignment of majority bonuses in Cascadia.
 */
public class MajorityBonus {
	private final Map<Player, Map<Habitat, Integer>> bonuses;

	/**
	 * Constructs a MajorityBonus and immediately calculates bonuses.
	 *
	 * @param players   the list of players in the game
	 * @param nbPlayers the number of players
	 * @throws NullPointerException     if players is null
	 * @throws IllegalArgumentException if nbPlayers is less than 1
	 */
	public MajorityBonus(Collection<Player> players, int nbPlayers) {
		Objects.requireNonNull(players, "Players cannot be null");
		if (nbPlayers < 1) {
			throw new IllegalArgumentException("Number of players must be at least 1");
		}
		this.bonuses = calculateBonuses(players, nbPlayers);
	}

	/**
	 * Retrieves the computed bonuses.
	 *
	 * @return the map of players to their habitat bonuses
	 */
	public Map<Player, Map<Habitat, Integer>> getBonuses() {
		return bonuses;
	}

	private Map<Player, Map<Habitat, Integer>> calculateBonuses(Collection<Player> players, int nbPlayers) {
	  var bonusMap = new HashMap<Player, Map<Habitat, Integer>>();

	  if (nbPlayers == 1) {
	    Player soloPlayer = players.iterator().next();
	    calculateSoloBonuses(soloPlayer, bonusMap);
	  } else {
	    Habitat.allHabitats().forEach(habitat -> {
	      calculateBonusesForHabitat(players, habitat, nbPlayers, bonusMap);
	    });
	  }

	  return bonusMap;
	}


	private void calculateBonusesForHabitat(Collection<Player> players, Habitat habitat, int nbPlayers,
			Map<Player, Map<Habitat, Integer>> bonusMap) {
		var scoresByPlayer = getScoresForHabitat(players, habitat);
		int maxScore = getMaxScore(scoresByPlayer);
		var topPlayers = getTopPlayers(scoresByPlayer, maxScore);

		if (nbPlayers == 2) {
			applyTwoPlayerRules(habitat, topPlayers, bonusMap);
		} else {
			applyThreeOrFourPlayerRules(habitat, scoresByPlayer, maxScore, bonusMap);
		}
	}

	private Map<Player, Integer> getScoresForHabitat(Collection<Player> players, Habitat habitat) {
		return players.stream().collect(Collectors.toMap(Function.identity(),
				player -> player.getScoreCalculator().calculateTilesScore().getOrDefault(habitat, 0)));
	}

	private int getMaxScore(Map<Player, Integer> scoresByPlayer) {
		return scoresByPlayer.values().stream().mapToInt(Integer::intValue).max().orElse(0);
	}

	private List<Player> getTopPlayers(Map<Player, Integer> scoresByPlayer, int maxScore) {
		return scoresByPlayer.entrySet().stream().filter(entry -> entry.getValue() == maxScore).map(Map.Entry::getKey)
				.toList();
	}

	private void applyTwoPlayerRules(Habitat habitat, List<Player> topPlayers,
			Map<Player, Map<Habitat, Integer>> bonusMap) {
		int bonus = (topPlayers.size() > 1) ? 1 : 2;
		topPlayers.forEach(player -> addBonus(player, habitat, bonus, bonusMap));
	}

	private void applyThreeOrFourPlayerRules(Habitat habitat, Map<Player, Integer> scoresByPlayer, int maxScore,
			Map<Player, Map<Habitat, Integer>> bonusMap) {
		var topPlayers = getTopPlayers(scoresByPlayer, maxScore);

		if (topPlayers.size() > 1) {
			topPlayers.forEach(player -> addBonus(player, habitat, 2, bonusMap));
		} else {

			var firstPlayer = topPlayers.get(0);
			addBonus(firstPlayer, habitat, 3, bonusMap);

			int secondMaxScore = getSecondMaxScore(scoresByPlayer, maxScore);
			var secondPlayers = getTopPlayers(scoresByPlayer, secondMaxScore);
			secondPlayers.forEach(player -> addBonus(player, habitat, 1, bonusMap));
		}
	}

	private int getSecondMaxScore(Map<Player, Integer> scoresByPlayer, int maxScore) {
		return scoresByPlayer.values().stream().filter(score -> score < maxScore).mapToInt(Integer::intValue).max()
				.orElse(0);
	}

	private void calculateSoloBonuses(Player player, Map<Player, Map<Habitat, Integer>> bonusMap) {
		var habitatScores = player.getScoreCalculator().calculateTilesScore();
		habitatScores.forEach((habitat, score) -> {
			if (score >= 7) { 
				addBonus(player, habitat, 2, bonusMap);
			}
		});
	}

	private void addBonus(Player player, Habitat habitat, int bonus, Map<Player, Map<Habitat, Integer>> bonusMap) {
		bonusMap.computeIfAbsent(player, p -> new HashMap<>()).merge(habitat, bonus, Integer::sum);
	}

}
