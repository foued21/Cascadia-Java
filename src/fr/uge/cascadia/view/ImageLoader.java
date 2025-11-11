package fr.uge.cascadia.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;

/**
 * Utility class to load and cache images for the Cascadia game.
 * 
 * <p>
 * This class loads all images from the "resources/images" directory at
 * application startup and stores them in a cache for efficient retrieval during
 * the game.
 * </p>
 */
public class ImageLoader {
	private final Map<String, BufferedImage> imageCache = new HashMap<>();

	/**
	 * Preloads all images from the "resources/images" directory.
	 * 
	 * @throws RuntimeException if an error occurs during image loading
	 */
	public void preloadImages() {
		try {
			var resource = ImageLoader.class.getResource("/images/");
			if (resource == null) {
				throw new IllegalArgumentException("The images directory does not exist in resources.");
			}

			if ("jar".equals(resource.getProtocol())) {
				loadFromJar(resource.getPath());
			} else {
				loadFromFileSystem(resource.getPath());
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to preload images from /images directory", e);
		}
	}

	private void loadFromJar(String jarPath) throws IOException {
		String jarFilePath = jarPath.substring(5, jarPath.indexOf("!"));
		try (var jarFile = new JarFile(jarFilePath)) {
			var entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().startsWith("images/")
						&& (entry.getName().endsWith(".png") || entry.getName().endsWith(".jpg"))) {
					try (var inputStream = jarFile.getInputStream(entry)) {
						var image = ImageIO.read(inputStream);
						var key = entry.getName().substring("images/".length()).toLowerCase();
						imageCache.put(key, image);
					}
				}
			}
		}
	}

	private void loadFromFileSystem(String path) throws IOException {
		var dir = Paths.get(path);
		try (var stream = Files.walk(dir)) {
			stream.filter(Files::isRegularFile)
					.filter(file -> file.toString().endsWith(".png") || file.toString().endsWith(".jpg")).forEach(file -> {
						try {
							var image = ImageIO.read(file.toFile());
							var key = file.getFileName().toString().toLowerCase();
							imageCache.put(key, image);
						} catch (IOException e) {
							throw new RuntimeException("Failed to load image: " + file, e);
						}
					});
		}
	}

	/**
	 * Retrieves a preloaded image from the cache.
	 * 
	 * @param imageName the name of the image file (e.g., "fo.jpg" or
	 *                  "fo_ri.png")
	 * @return the {@link BufferedImage} for the given image name
	 * @throws IllegalStateException if the image is not found in the cache
	 */
	public BufferedImage getImage(String imageName) {
		Objects.requireNonNull(imageName, "Image name cannot be null");
		var key = imageName.toLowerCase();
		var image = imageCache.get(key);
		return image;
	}
}
