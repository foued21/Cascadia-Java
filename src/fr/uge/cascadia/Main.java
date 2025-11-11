package fr.uge.cascadia;

import fr.uge.cascadia.game.GameManager;
import fr.uge.cascadia.view.ImageLoader;

public class Main {
	public static void main(String[] args) {
		var imageLoader = new ImageLoader();
		imageLoader.preloadImages();
		GameManager.startGame(imageLoader);
	}
}
