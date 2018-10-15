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
	private ArrayList<Brick> Obstacles;
	private BufferedImage bg = Loader.LoadImage("bg.png");
	private ArrayList<AudioPlayer> sounds = new ArrayList<AudioPlayer>();
	private AudioPlayer PointSound = new AudioPlayer("sfx_point.mp3");
	private AudioPlayer DeathSound = new AudioPlayer("sfx_hit.mp3");
	private AudioPlayer Music = new AudioPlayer("Music.mp3");
	private float minutes, minutesP;
	private int scored, score, ticks, ticks2, topScore, volume = 15;
	private boolean wasPaused;
	
	public LevelState() {
		Music.SetVolume(0.15f);
		PointSound.SetVolume(0.85f);
		DeathSound.SetVolume(0.25f);
		sounds.add(Music);
		sounds.add(PointSound);
		sounds.add(DeathSound);
		IO.checkIfExist();
		topScore = 0;
	}
	
	public void init() {
		topScore = Integer.parseInt(IO.getHS());
		player = new Player(100, 100);
		Obstacles = new ArrayList<Brick>();
		score = scored = 58;
		ticks = ticks2 = 0;
		minutes = 0f;
		player.init();
		paused = won = wasPaused = false;
		Obstacles.add(new Brick());
		Obstacles.get(0).init();
	}

	public void update() {
		Music.SetVolume((float) volume / 100);
		DeathSound.SetVolume((float) volume / 100);
		PointSound.SetVolume((float) volume / 100);
		player.setVolume((float) volume / 100);
		if (paused) {
			Music.stop();
			wasPaused = true;
			return;
		}
		if (won) {
			IO.writeHS(topScore);
			Music.stop();
			return;
		}
		minutes = Game.Seconds / 60 + minutesP;
		if (Game.Seconds == 60) {
			minutes++;
			minutesP++;
			Game.Seconds = 0;
		}
		if (!Music.isActive()) {
			if (!wasPaused) Music.play();
			else {
				Music.restart();
				wasPaused = false;
			}
		}
		player.update();
		if (Obstacles.get(Obstacles.size() - 1).getX() < 550 + (score * 2)) {
			if (score > 70) return;
			Brick brick = new Brick();
			brick.init();
			Obstacles.add(brick);
			brick.setVelocity(5 + score / 15);
		}
		for (Brick brick : Obstacles) {
			brick.update();
			if (brick.getX() < player.getX() && !brick.hasCounted()) {
				score++;
				scored++;
				if (score == 60) {
					won = true;
				}
				brick.SetHasCounted(true);
				if (score == 5 || scored == 5) {
					PointSound.play();
					scored = 0;
				}
			}
			if (player.getHitBox().intersects(brick.getHitBox()) || player.getHitBox().intersects(brick.GetSecondHitBox())) {
				player.setDead(true);
			}
			if (score <= 45) brick.setVelocity(5 + score / 15);
		}
		if ((Obstacles.get(0).getX() + 64) < 0) {
			Obstacles.remove(0);
		}
	}

	public void render(Graphics2D g) {
		if (won) {
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			g.scale((float) ticks2 / 100, (float) ticks2 / 100);
			g.drawImage(Assets.Won, 0, 0, Game.WIDTH, Game.HEIGHT, null);
			g.setColor(Color.red);
			g.drawString("Press Esc to restart!", 10, 25);
			if (!PointSound.isActive() || ticks >= 35) {
				PointSound.play();
				ticks = 0;
			}
			if (score > topScore) {
				g.drawString("New Best Score: " + score + ", Previous best score: " + topScore, 10, Game.HEIGHT - 25);
			}
			ticks++;
			if (ticks2 != 100) ticks2++;
			return;
		}
		if (player.isDead()) {
			DeathSound.play();
			Game.Seconds = 0;
			minutes = 0;
			if (score > topScore) {
				IO.writeHS(score);
			}
			init();
		}
		g.drawImage(bg, 0, 0, 800, 600, null);
		player.render(g);
		for (Brick brick : Obstacles) {
			brick.render(g);
		}
		if (paused) {
			g.setColor(Color.red);
			g.drawString("PAUSED", 340, 275);
			g.drawString("Volume: " + volume + " (Use the arrow keys to modify it)", 175, 315);
		}
		g.setColor(Color.red);
		g.drawString("Score: " + score, 10, 30);
		if ((int) minutes == 0) g.drawString("Time: " + (int)Game.Seconds + "s", 10, 90);
		else  g.drawString("Time: " + (int)minutes + ":" + (int)Game.Seconds + "s", 10, 90);
		g.drawString("Best Score: " + IO.getHS(), 10, 60);
	}

	public void KeyPressed(int keyCode) {
		if (won) { 
			if (keyCode == KeyEvent.VK_ESCAPE) init();
			return;
		}
		if (keyCode == KeyEvent.VK_ESCAPE) {
			paused = !paused;
		}
		if (paused) {
			if (keyCode == KeyEvent.VK_UP) {
				if (volume == 100) return;
				volume++;
			}
			else if (keyCode == KeyEvent.VK_DOWN) {
				if (volume == 0) return;
				volume--;
			}
		}
	}
	
	public void mouseClicked(int button) {
		if (paused || won) return;
		player.MouseClicked(button);
	}
	
	public void mouseReleased(int button) {
		if (paused || won) return;
		player.MouseReleased(button);
	}

}
