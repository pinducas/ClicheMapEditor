package tools;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Loader {

	public BufferedImage loadImage(String path){
		try{
			return(ImageIO.read(getClass().getResource("/"+path)));
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Could not load something");
			System.exit(0);
			return null;	
		}
	}
	
}
