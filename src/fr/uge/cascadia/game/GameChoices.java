package fr.uge.cascadia.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides utility methods for gathering player input and game settings.
 *
 * <p>
 * The {@code GameChoices} class handles user interaction to configure the game,
 * including selecting the game variant, mode, number of players, and player
 * names.
 * </p>
 */
public class GameChoices {

	/**
	 * Prompts the user to choose a game variant.
	 *
	 * <p>
	 * The available variants are:
	 * <ul>
	 * <li>1 - Family mode</li>
	 * <li>2 - Intermediate mode</li>
	 * <li>3 - Animal cards mode</li>
	 * </ul>
	 *
	 * @return the selected variant as an integer (1, 2, or 3)
	 */
	public static int variantChoice() {
		System.out.println("Choose Variant for the game:\n1 for Family\n2 for Intermediate\n3 for animal cards");
		@SuppressWarnings("resource")
		var scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("Your choice: ");
				int choice = Integer.parseInt(scanner.next());
				if (choice == 1 || choice == 2 || choice == 3) {
					return choice;
				} else {
					System.out.println("Invalid choice. Please enter 1, 2 or 3.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number (1, 2 or 3).");
			}
		}
	}

	/**
	 * Prompts the user to choose a card for the "Animal cards" game mode.
	 *
	 * <p>
	 * The available cards are:
	 * <ul>
	 * <li>A</li>
	 * <li>B</li>
	 * <li>C</li>
	 * <li>D</li>
	 * </ul>
	 *
	 * @return the selected card as an uppercase string ("A", "B", "C", or "D")
	 */
	public static String cardChoice() {
		System.out.println("Choose a card for animal cards: A, B, C, D");
		@SuppressWarnings("resource")
		var scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("Your choice: ");
				String choice = scanner.next().toUpperCase();
				if (choice.equals("A") || choice.equals("B") || choice.equals("C") || choice.equals("D")) {
					return choice;
				} else {
					System.out.println("Invalid choice. Please enter A, B, C, or D.");
				}
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter A, B, C, or D.");
			}
		}
	}

	/**
	 * Prompts the user to choose a game mode.
	 *
	 * <p>
	 * The available modes are:
	 * <ul>
	 * <li>1 - Play in the terminal</li>
	 * <li>2 - Play with a graphical interface (square tiles)</li>
	 * <li>3 - Play with a graphical interface (hexagonal tiles)</li>
	 * </ul>
	 *
	 * @return the selected mode as an integer (1, 2, or 3)
	 */
	public static int gameModeChoice() {
		System.out.println(
				"Choose the game Mode :\n1 - Play in Terminal\n2 - Play with Graphical Interface square tiles\n3 - Play with Graphical Interface hexagonal tiles");
		@SuppressWarnings("resource")
		var scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("Your choice: ");
				int choice = Integer.parseInt(scanner.next());
				if (choice == 1 || choice == 2 || choice == 3) {
					return choice;
				} else {
					System.out.println("Invalid choice. Please enter 1 or 2.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number (1 or 2).");
			}
		}
	}

	public static int modeChoice() {
		System.out.println(
				"Choose the Mode :\n1 - Solo\n2 - Multiplayer");
		@SuppressWarnings("resource")
		var scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("Your choice: ");
				int choice = Integer.parseInt(scanner.next());
				if (choice == 1 || choice == 2) {
					return choice;
				} else {
					System.out.println("Invalid choice. Please enter 1 or 2.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number (1 or 2).");
			}
		}
	}

	/**
	 * Prompts the user to choose the number of players for the game.
	 *
	 * <p>
	 * The allowed range for the number of players is between 1 and 4 inclusive.
	 *
	 * @return the number of players as an integer (1, 2, 3, or 4)
	 */
	public static int numberPlayersChoice() {
		System.out.println("Choose the number of players (between 2 and 4)");
		@SuppressWarnings("resource")
		var scanner = new Scanner(System.in);
		while (true) {
			try {
				int choice = Integer.parseInt(scanner.next());
				if (choice >= 2 && choice <= 4) {
					return choice;
				} else {
					System.out.println("Invalid choice. Please select a number between 2 and 4.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number between 2 and 4.");
			}
		}
	}

	/**
	 * Prompts the user to enter the names of all players.
	 *
	 * <p>
	 * The number of names requested corresponds to the number of players selected.
	 * Each name must be non-empty, and duplicate names are allowed.
	 *
	 * @param nbPlayers the number of players
	 * @return a {@link List} of player names as strings
	 */
	public static List<String> playerNames(int nbPlayers) {
		var names = new ArrayList<String>();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the names of the " + nbPlayers + " players:");
		for (int i = 1; i <= nbPlayers; i++) {
			System.out.print("Player " + i + " name: ");
			String name = scanner.nextLine().trim();
			while (name.isEmpty()) {
				System.out.println("Name cannot be empty. Please enter a valid name.");
				System.out.print("Player " + i + " name: ");
				name = scanner.nextLine().trim();
			}
			names.add(name);
		}
		return names;
	}
}
