package States;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import GameObject.Brick;
import GameObject.Player;
import Main.Game;
import Utilities.Assets;
import Utilities.AudioPlayer;
import Utilities.IO;
import Utilities.Loader;

public class LevelState{
	
	private Player player;
	private boolean paused, won;
	private ArrayList<Brick> Obstacles; // The Obstacles/tubes to update and render
	private BufferedImage bg = Loader.LoadImage("bg.png"); // The background image
	private AudioPlayer PointSound = new AudioPlayer("sfx_point.mp3");
	private AudioPlayer DeathSound = new AudioPlayer("sfx_hit.mp3");
	private AudioPlayer Music = new AudioPlayer("Music.mp3");
	private float minutes, minutesP;
	private int scored, score, ticks, ticks2, topScore, volume = 15;
	private boolean wasPaused;
	
	public LevelState() {
		Music.SetVolume(0.15f); // this happens only when the game is first started!
		PointSound.SetVolume(0.85f);
		DeathSound.SetVolume(0.25f);
		IO.checkIfExist(); // Checks if the file to save the record exists, if it doesn't it creates one
		topScore = 0;
	}
	// called when player is dead
	public void init() {
		topScore = Integer.parseInt(IO.getHS()); // reads the top score from the record file turning it to an int
		player = new Player(100, 100); // reset player's stats and position
		Obstacles = new ArrayList<Brick>(); // clears the obstacles
		score = scored = 0; // sets the score to 0
		ticks = ticks2 = 0;
		minutes = 0f;
		player.init(); 
		paused = won = wasPaused = false;
		Obstacles.add(new Brick()); // insert a new Obstacle manually
		Obstacles.get(0).init();
	}

	public void update() {
		if (paused) {
			Music.stop(); // if the game is paused, stop the music
			wasPaused = true;
			return;
		}
		if (won) {
			IO.writeHS(60); // if the player won, write the maximum score (60) to the record file
			Music.stop(); // and stop the music
			return;
		}
		minutes = Game.Seconds / 60 + minutesP; // calculates
		if (Game.Seconds == 60) { // the
			minutes++; // time 
			minutesP++; // for
			Game.Seconds = 0; // rendering it on the screen
		}
		if (!Music.isActive()) {
			if (!wasPaused) Music.play(); // checks for music loop
			else {
				Music.restart(); // and start it again from the point it was left
				wasPaused = false; // if the player has paused the game
			}
		}
		player.update();
		if (Obstacles.get(Obstacles.size() - 1).getX() < 550 + (score * 2)) { // if the obstacle the most distant
			if (score > 70) return; // from the player has an x < 550 (pixels) + double of the score, and the score isn't more than 70
			Brick brick = new Brick(); // create a new Obstacle
			brick.init(); // and add it to the database to update and render it
			Obstacles.add(brick);
			brick.setVelocity(5 + score / 15);
		}
		for (Brick brick : Obstacles) { // Loop trought every obstacle
			brick.update(); // updates (move) the obstacle
			if (brick.getX() < player.getX() && !brick.hasCounted()) { // checks for the player to have scored
				score++;
				scored++;
				if (score == 60) {
					won = true; // checks for the player's win
				}
				brick.SetHasCounted(true);
				if (score == 5 || scored == 5) {
					PointSound.play(); // plays the point sound every 5 obstacles passed
					scored = 0;
				}
			}
			if (player.getHitBox().intersects(brick.getHitBox()) || player.getHitBox().intersects(brick.GetSecondHitBox())) {
				player.setDead(true); // checks if the player has hit an obstacle, if so kill him
			}
			if (score <= 45) brick.setVelocity(5 + score / 15); // if the score is less than 45, add to the normal player speed
		} // the score divided by 15
		if ((Obstacles.get(0).getX() + 64) < 0) { // if the obstacle is out of the screen, remove it from the database
			Obstacles.remove(0); // clears RAM and makes sure that the tubes that aren't in the screen aren't rendered/drawn
		} // or updated
	}

	public void render(Graphics2D g) {
		if (won) { // if the player won
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT); // clears manually the screen every frame
			g.scale((float) ticks2 / 100, (float) ticks2 / 100); // makes the winning image pop up gradually
			g.drawImage(Assets.Won, 0, 0, Game.WIDTH, Game.HEIGHT, null); // from a width and height of 0
			g.setColor(Color.red); // to the width and height of the screen
			g.drawString("Press Esc to restart!", 10, 25); // gives instructions to restart
			if (!PointSound.isActive() || ticks >= 35) { // plays the point/score sound infinitely
				PointSound.play();
				ticks = 0;
			}
			if (score > topScore) { // if the score is higher than the last record score, say id
				g.drawString("New Best Score: " + score + ", Previous best score: " + topScore, 10, Game.HEIGHT - 25);
			}
			ticks++;
			if (ticks2 != 100) ticks2++; // make sure the image doesn't becomes bigger than the screen size
			return;
		}
		if (player.isDead()) { // if the player is dead
			DeathSound.play(); // play the death sound
			Game.Seconds = 0; // reset the timer
			minutes = 0;
			if (score > topScore) { // and if the score is more than the top score write it as a record
				IO.writeHS(score);
			}
			init(); // and restart the game/level, witouth loading resources again obviously
		}
		g.drawImage(bg, 0, 0, 800, 600, null); // render the background image
		player.render(g); // render the player image
		for (Brick brick : Obstacles) { // render all the tubes, obstacles
			brick.render(g);
		}
		if (paused) { // if the game is paused
			g.setColor(Color.red);
			g.drawString("PAUSED", 340, 275); // draw the PAUSED sign
			g.drawString("Volume: " + volume + " (Use the arrow keys to modify it)", 175, 315); // and the volume
		}
		g.setColor(Color.red);
		g.drawString("Score: " + score, 10, 30); // draw the score
		if ((int) minutes == 0) g.drawString("Time: " + (int)Game.Seconds + "s", 10, 90); // and the timer
		else  g.drawString("Time: " + (int)minutes + ":" + (int)Game.Seconds + "s", 10, 90);
		g.drawString("Best Score: " + IO.getHS(), 10, 60); // and the best score
	}

	public void KeyPressed(int keyCode) {
		if (won) { 
			if (keyCode == KeyEvent.VK_ESCAPE) init(); // if the player won restart the level whenever the user press ESC
			return;
		}
		if (keyCode == KeyEvent.VK_ESCAPE) {
			paused = !paused; // checks for the input to pause the game
		}
		if (paused) {
			if (keyCode == KeyEvent.VK_UP) { // if the player presses the up arrow
				if (volume == 100) return; // if the volume is more than 100 just do nothing
				volume++; // adds 1 to the volume
				Music.SetVolume((float) volume / 100); // and sets the volume to all the sounds
				DeathSound.SetVolume((float) volume / 100); // it's just 4, i can do this manually
				PointSound.SetVolume((float) volume / 100); // no need for an arraylist
				player.setVolume((float) volume / 100);
			}
			else if (keyCode == KeyEvent.VK_DOWN) {
				if (volume == 0) return; // same to take the volume lower
				volume--;
				Music.SetVolume((float) volume / 100);
				DeathSound.SetVolume((float) volume / 100);
				PointSound.SetVolume((float) volume / 100);
				player.setVolume((float) volume / 100);
			}
		}
	}
	
	public void mouseClicked(int button) { // called when the mouse is clicked
		if (paused || won) return; // if the game is paused or the user won just do nothing
		player.MouseClicked(button); // check for the input in the player's class
	}
	
	public void mouseReleased(int button) {
		if (paused || won) return; // same as mouseClicked
		player.MouseReleased(button);
	}

}
