package GameObject;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GameObject {
	
	protected float x, y;
	protected float width, height;
	protected float dx, dy;
	
	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void update() {}
	
	public void render(Graphics2D g) {}
	
	public Rectangle getHitBox() {
		return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}

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
