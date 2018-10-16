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
	
	public static int WIDTH;
	public static int HEIGHT;
	private String title;
	
	private JFrame frame; // the window
	private Canvas canvas; // the canvas, basically we are drawing things on it and putting it on our window :D
	
	private Thread thread; // the Thread/Process of the game, makes the performance not be shit basically
	private boolean running; // wether the program is running or not
	
	private Graphics2D g; // the magic thing used to draw textures and other stuff on the canvas
	private BufferStrategy bs; // a bufferStrategy, can't explain what it is with a comment, just know it's useful for stuff
	
	private LevelState Level; // the class of the Level (the actual game)
	private BufferedImage Logo; // the Icon of the gam
	
	public static float Seconds; // the game timer
	
	public Game(int width, int height, String title) {
		Game.WIDTH = width;
		Game.HEIGHT = height;
		this.title = title;
		Logo = Loader.LoadImage("Images.png").getSubimage(0, 0, 64, 64); // Loads the image from the spriteSheet
		Level = new LevelState();
		createDisplay(); // a private function to create the window and initialize the basic stuff
	}
	
	private void init() {
		Seconds = 0;
		Assets.init(); // Loads the textures
		Level.init(); // initialize the level
		running = true; // the game is now actually running
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
				render(); // makes sure the game is rendered 60 times a seconds
				ticks++;
				delta--;
			}
			
			if(timer >= 1000000000){
				System.out.println("Ticks and Frames: " + ticks); // prints the fps done if a second is passed
				ticks = 0;
				timer = 0;
				Seconds++; // and updates the game timer
			}
		}
		
		stop(); // stops the game
	}
	
	private void update() {
		Level.update();
	}
	
	private void render() {
		bs = canvas.getBufferStrategy(); // stuff i can't explain with a single comment
		if (bs == null) {
			canvas.createBufferStrategy(2); // other stuff i can't explain with a single comment
			return;
		}
		g = (Graphics2D) bs.getDrawGraphics(); // other other stuff i can't explain with a single comment
		g.clearRect(0, 0, HEIGHT, WIDTH); // clears the screen every frame, if i don't every frame you'll see a new player rendered, 300 players in 5s :D
		
		Level.render(g); // renders the actual game
		
		g.dispose(); // releases the resources of the graphics object
		bs.show(); // and show the thing i can't explain with a comment (it's REALLY BASICALLY a canvas in the actual computer, we draw suff on this, then it goes to our actual Canvas object)
	}
	
	public synchronized void start() {
		if (running) return;
		thread = new Thread(this); // Initialize the thread/process
		canvas.addKeyListener(this); // adds the keyListener and MouseListener for the mouse and keyboard
		canvas.addMouseListener(this); // input on the canvas
		thread.start(); // and starts the thread process
	}
	
	public synchronized void stop() {
		try {
			thread.join(); // stops the thread releasing all of its RAM and resources
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void createDisplay() {
		frame = new JFrame(title); // create a new Object of the window, with its title
		frame.setSize(WIDTH, HEIGHT); // sets the size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes sure the window actually closes when the game is closed
		frame.setResizable(false); // makes sure the user can't set the size of the window
		frame.setAlwaysOnTop(false); // just to make so that if you click somewhere off the window it hides behind other ones, to make you lose ehehhehe
		frame.setFocusable(false); // gonna explain this with the canvas, basically just freeing some RAM
		frame.requestFocus(); // to actually get the input, if i am writing something on notepad and i press the mouse the player will not jump this way
		frame.setIconImage(Logo); // sets the image of the window the the logo image
		frame.setVisible(true); // makes sure the window is visible (yes, at default it isn't xD)
		
		canvas = new Canvas(); // Initializes the canvas
		canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // sets the dimension to the screen's one
		canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		canvas.setFont(new Font("Calibri", Font.PLAIN, 25)); // set's the font i want it to have
		canvas.setFocusable(true);  // now we can request the focus on the window (it must be the last thing we clicked on
		canvas.requestFocus(); // to actually get the input, if i am writing something on notepad and i press the mouse the player
		frame.add(canvas); // adds the canvas to the window
		frame.pack(); // and makes sure the window basically adapts to its components
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Level.KeyPressed(e.getKeyCode()); // gets the input and gets called when a key is pressed (cause of KeyListener)
	}
	
	@Override
	public void keyReleased(KeyEvent e) { }
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Level.mouseClicked(e.getButton()); // gets the input and gets called when a mouse button is pressed (cause of MouseListener)
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) {	}

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) {
		Level.mouseReleased(e.getButton()); // gets the input and gets called when a mouse button is released (cause of MouseListener)
	}
	
}
