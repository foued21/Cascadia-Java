package fr.uge.cascadia.score;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/**
 * Manages success achievements by loading and retrieving them from a resource file.
 */
public class SuccessManager {
	private final Map<Integer, String> successes = new HashMap<>();

	/**
	 * Loads successes from a resource file into the instance map.
	 *
	 * @param resourcePath the path to the resource file (e.g.,
	 *                     "/success/successes.txt")
	 * @throws IOException if an I/O error occurs
	 */
	public void loadSuccesses() throws IOException {
    var resourcePath = "/success/successes.txt";
    try (InputStream inputStream = getClass().getResourceAsStream(resourcePath);
         var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid success format: " + line);
            }

            int number = Integer.parseInt(parts[0].trim());
            var description = parts[1].trim();

            successes.put(number, description);
        }
    }
}


	/**
	 * Retrieves the description of a success by its number.
	 *
	 * @param number the number of the success
	 * @return the description of the success
	 * @throws IllegalArgumentException if the success number does not exist
	 */
	public String getSuccessDescription(int number) {
		String description = successes.get(number);
		if (description == null) {
			throw new IllegalArgumentException("Success with number " + number + " does not exist");
		}
		return description;
	}

	/**
	 * Retrieves all successes.
	 *
	 * @return the map of successes
	 */
	public Map<Integer, String> getAllSuccesses() {
		return new HashMap<>(successes); // Return a copy to avoid external modifications
	}
}
