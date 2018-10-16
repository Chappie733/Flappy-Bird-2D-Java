package Utilities;

import java.awt.image.BufferedImage;

public class Assets {
	
	private static BufferedImage source = Loader.LoadImage("Images.png"); // Loads the image to then get all the others
	
	public static BufferedImage bird, brick, Won;
	
	public static void init() {
		
		bird = source.getSubimage(0, 0, 64, 64); // the player/bird image
		brick = source.getSubimage(64, 0, 64, 150); // the obstacle/tube image
		Won = Loader.LoadImage("Win.png"); // the image that displays when you win (at 60 points)
		
	}
}
