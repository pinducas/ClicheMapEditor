package tools;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Loader {

	public BufferedImage loadInsideImage(String path){
		try{
			return(ImageIO.read(getClass().getResource("/"+path)));
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Could not load something");
			return null;	
		}
	}
	public BufferedImage loadImage(String path){
		try{
			File f = new File(path);
			return(ImageIO.read(f));
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Could not load something");
			return null;	
		}
	}
}
