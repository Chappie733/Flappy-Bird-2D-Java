package GameObject;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Main.Game;
import Utilities.Assets;
import Utilities.AudioPlayer;

public class Player extends GameObject {
	
	private boolean jumped, hasJumped, dead;
	private int timer;
	private BufferedImage bird;
	private AudioPlayer SwingSound = new AudioPlayer("sfx_wing.mp3");
	
	public Player(float x, float y) {
		super(x, y);
		SwingSound.SetVolume(0.25f);
	}
	
	public void init() {
		dx = 0;
		dy = 3;
		width = height = 32;
		jumped = hasJumped = false;
		bird = Assets.bird;
	}
	
	public void update() {
		if (y < 0 || y > Game.HEIGHT) dead = true;
		if (jumped) {
			hasJumped = true;
			jumped = false;
		}
		if (hasJumped) {
			if (timer < 15) {
				dy = -3.5f;
				jumped = false;
			} else {
				dy = 0.1f;
				hasJumped = false;
			}
		}
		dy *= 1.15f;
		if (dy > 5f) dy = 5f;
		y += dy;
		timer++;
	}
	
	public void render(Graphics2D g) {
		if (dead) return;
		g.drawImage(bird, (int) x, (int) y, (int) width, (int) height, null);
	}
	
	public void MouseClicked(int button) {
		if (button == MouseEvent.BUTTON1) {
			jumped = true; 
		}
	}
	
	public void MouseReleased(int button) {
		if (button == MouseEvent.BUTTON1) {
			jumped = true;
			SwingSound.play();
			timer = 0;
		}
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	public void setVolume(float volume) {
		SwingSound.SetVolume(volume);
	}
}
