package core;

import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TextBox extends JTextField{

	public TextBox(JPanel panel,int x, int y,int width,int height, String text){
		this.setText(text);
		this.setBounds(new Rectangle(x, y, width, height));
		
		panel.add(this);
	}
	
	public void update(){
		
	}
	
}
