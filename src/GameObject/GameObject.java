package GameObject;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GameObject {
	
	protected float x, y; // x and y position in the window (in pixels)
	protected float width, height; // width and height of the object (in pixels)
	protected float dx, dy; // the direction in the x and y axis
	// Constructor, called when you create an object of the class (Example: GameObject obj = new GameObject(100, 100);
	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void update() {}
	
	public void render(Graphics2D g) {}
	
	public Rectangle getHitBox() {
		return new Rectangle((int) x, (int) y, (int) width, (int) height); // gets the hitbox
	}
	// Getters and setters
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getDx() {
		return dx;
	}

	public float getDy() {
		return dy;
	}
	
	
}
