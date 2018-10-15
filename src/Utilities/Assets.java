package Utilities;

import java.awt.image.BufferedImage;

public class Assets {
	
	private static BufferedImage source = Loader.LoadImage("Images.png");
	
	public static BufferedImage bird, brick, Won;
	
	public static void init() {
		
		bird = source.getSubimage(0, 0, 64, 64);
		brick = source.getSubimage(64, 0, 64, 150);
		Won = Loader.LoadImage("Win.png");
		
	}
}
