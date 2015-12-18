package core;

import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TextBox extends JTextField{

	private Box box;
	
	public TextBox(JPanel panel, Box box, int x, int y,int width,int height, String text){
		this.setText(text);
		
		this.setBounds(new Rectangle(x, y, width, height));
		
		panel.add(this);
		
		this.box = box;
	}
	
	public void update(){
		String a = this.getText();
		if(a.length() > 0){
			try{
				
				int value = Integer.parseInt(a);
				
				if(value < 480){
					box.setWidth(value);
					box.setHeight(value);
				}
				
			}catch(Exception e){
				this.setText(""+box.getWidth());
			}	
		}
		
	}
	
}
