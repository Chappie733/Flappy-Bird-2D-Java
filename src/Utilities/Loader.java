package Utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
public class Loader {
	
	public static BufferedImage LoadImage(String path) {
		try {
			return ImageIO.read(Loader.class.getResource("/res/" + path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
