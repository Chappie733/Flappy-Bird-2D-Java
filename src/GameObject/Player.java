package GameObject;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Main.Game;
import Utilities.Assets;
import Utilities.AudioPlayer;

public class Player extends GameObject { // the player is a gameObject, so extend that class
	
	private boolean jumped, hasJumped, dead; // jumped, to know wheter the player jumped or not, hasJumped, to know wheter the player can jump again or has to wait
	private int timer;
	private BufferedImage bird;
	private AudioPlayer SwingSound = new AudioPlayer("sfx_wing.mp3"); // the sound of the jump
	// this time we can load it directly in the class cause there is just going to be one instance of the player, still is improvable
	public Player(float x, float y) {
		super(x, y);
		SwingSound.SetVolume(0.25f);
	}
	
	public void init() {
		dx = 0;
		dy = 3; // when it starts it's falling
		width = height = 32; // sets the width and the height to 32 both to 32 pixels
		jumped = hasJumped = false; // the player isn't jumping when the game starts, or he respawns
		bird = Assets.bird; // loads the bird image/texture
	}
	
	public void update() {
		if (y < 0 || y > Game.HEIGHT) dead = true; // makes sure the player dies if it goes out of the screen
		if (jumped) { // if the player entered the input (space bar, stored in this variaable)
			hasJumped = true; // then the player jumped (meaning this will start the moving mechanic
			jumped = false; // and it makes sure it doesn't goes in this if again
		}
		if (hasJumped) { // jumping mechanics
			if (timer < 15) {
				dy = -3.5f; // for 15 ticks the speed will be -3.5, meaning -3.5 upwards
				jumped = false; // and the player will not be able to jump again
			} else {
				dy = 0.1f; // else first sets the direction to a positive number, meaning it starts to slowly fall
				hasJumped = false; // and the player isn't jumping anymore, it's now falling
			}
		}
		dy *= 1.15f; // multiplies the speed for 1.15 to make sure the player slowly accellerates while falling
		if (dy > 5f) dy = 5f;
		y += dy; // adds the speed to the position
		timer++; // adds 1 to the tick counter
	}
	
	public void render(Graphics2D g) {
		if (dead) return; // if the player is dead, just do nothing
		g.drawImage(bird, (int) x, (int) y, (int) width, (int) height, null); // otherwise draw/render the bird image at the right
	} // position
	
	public void MouseReleased(int button) { // and when he releases it set the timer to 0, to start the process
		if (button == MouseEvent.BUTTON1) { // and jump
			jumped = true;
			SwingSound.play(); // also start the jumping sound
			timer = 0;
		}
	}
	// getters and setters
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
