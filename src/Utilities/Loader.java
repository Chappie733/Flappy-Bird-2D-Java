package Utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
public class Loader {
	
	public static BufferedImage LoadImage(String path) {
		try {
			return ImageIO.read(Loader.class.getResource("/res/" + path)); // reads the texture from the resource folder and returns it
		} catch (IOException e) {
			e.printStackTrace(); // in case of an error, print it in the console
		}
		return null; // just added this to make sure the compilation doesn't crash, even if this is never gonna be reached,
	} // cause if the resource is null the game is just gonna crash, so it will never come to this return null;
}
