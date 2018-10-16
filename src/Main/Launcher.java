package Main;

public class Launcher {
	public static void main(String[] args) {
		Game game = new Game(800, 600, "Flappy Bird"); // Creates the Game class, sets the width(800), the height(600)
		game.start(); // and the title("Flappy Bird)" to the window, and starts the game's thread/process
	}
}
