package GameObject;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import Main.Game;
import Utilities.Assets;

public class Brick extends GameObject{ // it's a gameObject, so extend that class
	
	private Random rand = new Random(); // a new Random, to determine the height of the tubes
	private float y2, width2, height2, vel;
	private boolean hasCounted; // wheter the obstacle has been counted for the player's score
	private BufferedImage texture; // the texture of the tube
	
	public Brick() {
		super (800, 0); // the original position (the width of the screen, and 0 height
		width = width2 = 64; // sets the 2 widths of the upper and lower brick to 64 pixels
		y = 0; // sets the y to 0
		vel = 5; // the original speed is 5
	}
	
	public void init() {
		height = (float) (rand.nextInt(600 / 2 - 10)); // sets the height to a random number
		y2 = (float) (600 - rand.nextInt(600 / 2 - 10)); // sets the height to a random number
		height2 = Game.HEIGHT - y2;
		if ((600 - (height + height2)) < 90) init(); // makes sure that the space between the lower obstacle and the upper one is more than 90 pixels
		if ((600 - (height + height2)) > 170) init();// makes sure that the space between the lower obstacle and the upper one is less than 170 pixels
		hasCounted = false;
		texture = Assets.brick; // takes the texture from the Assets class, so to not load the image every brick i spawn
	}
	
	public void update() {
		x -= vel; // updates the position
	}
	
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) (x - (width / 2) - 10), (int) y, (int) width * 2, (int) height, null); // draws the two tube textures (upper and lower)
		g.drawImage(texture, (int) (x - (width / 2) - 10), (int) (y2 + height2), (int) width2 * 2, (int) -height2, null); // in the right positions
	}
	
	public Rectangle GetSecondHitBox() {
		return new Rectangle((int) x, (int) y2, (int) width, (int) height); // returns the hit box of the upper tube
	}
	// Getters and setters
	public void SetHasCounted(boolean hasCounted) {
		this.hasCounted = hasCounted;
	}
	
	public boolean hasCounted() { return hasCounted; }
	
	public void setVelocity(float vel) { this.vel = vel; }
}
