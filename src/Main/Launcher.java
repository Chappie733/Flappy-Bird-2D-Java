package Main;

public class Launcher {
	public static void main(String[] args) {
		Game game = new Game(800, 600, "Flappy Bird");
		game.start();
	}
}