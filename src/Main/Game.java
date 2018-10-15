package Main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import States.LevelState;
import Utilities.Assets;
import Utilities.Loader;

@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable, KeyListener, MouseListener{
	
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	private String title;
	
	private JFrame frame;
	private Canvas canvas;
	
	private Thread thread;
	private boolean running;
	
	private Graphics2D g;
	private BufferStrategy bs;
	
	private LevelState Level;
	private BufferedImage Logo;
	
	JSlider slider = new JSlider(JSlider.HORIZONTAL);
	public static float Seconds;
	
	public Game(int width, int height, String title) {
		Game.WIDTH = width;
		Game.HEIGHT = height;
		this.title = title;
		Logo = Loader.LoadImage("Images.png").getSubimage(0, 0, 64, 64);
		Level = new LevelState();
		createDisplay();
	}
	
	private void init() {
		Seconds = 0;
		Assets.init();
		Level.init();
		running = true;
		frame.add(slider);
	}
	
	@Override
	public void run() {
		init();
		
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running){
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if(delta >= 1){
				update();
				render();
				ticks++;
				delta--;
			}
			
			if(timer >= 1000000000){
				System.out.println("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
				Seconds++;
			}
		}
		
		stop();
	}
	
	private void update() {
		Level.update();
	}
	
	private void render() {
		bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(2);
			return;
		}
		g = (Graphics2D) bs.getDrawGraphics();
		g.clearRect(0, 0, HEIGHT, WIDTH);
		
		Level.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public synchronized void start() {
		if (running) return;
		thread = new Thread(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		thread.start();
	}
	
	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void createDisplay() {
		frame = new JFrame(title);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setAlwaysOnTop(false);
		frame.setFocusable(false);
		frame.setIconImage(Logo);
		frame.setVisible(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		canvas.setFont(new Font("Calibri", Font.PLAIN, 25));
		canvas.setFocusable(true);
		canvas.requestFocus();
		frame.add(canvas);
		frame.pack();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Level.KeyPressed(e.getKeyCode());
	}
	
	@Override
	public void keyReleased(KeyEvent e) { }
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Level.mouseClicked(e.getButton());
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) {	}

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) {
		Level.mouseReleased(e.getButton());
	}
	
}
