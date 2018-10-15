package GameObject;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import Main.Game;
import Utilities.Assets;

public class Brick extends GameObject{
	
	private Random rand = new Random();
	private float y2, width2, height2, vel;
	private boolean hasCounted;
	private BufferedImage texture;
	
	public Brick() {
		super (800, 0);
		width = width2 = 64;
		y = 0;
		vel = 5;
	}
	
	public void init() {
		height = (float) (rand.nextInt(600 / 2 - 10));
		y2 = (float) (600 - rand.nextInt(600 / 2 - 10));
		height2 = Game.HEIGHT - y2;
		if ((600 - (height + height2)) < 90) init();
		if ((600 - (height + height2)) > 170) init();
		hasCounted = false;
		texture = Assets.brick;
	}
	
	public void update() {
		x -= vel;
	}
	
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) (x - (width / 2) - 10), (int) y, (int) width * 2, (int) height, null);
		g.drawImage(texture, (int) (x - (width / 2) - 10), (int) (y2 + height2), (int) width2 * 2, (int) -height2, null);
	}
	
	public Rectangle GetSecondHitBox() {
		return new Rectangle((int) x, (int) y2, (int) width, (int) height);
	}
	
	public void SetHasCounted(boolean hasCounted) {
		this.hasCounted = hasCounted;
	}
	
	public boolean hasCounted() { return hasCounted; }
	
	public void setVelocity(float vel) { this.vel = vel; }
}
