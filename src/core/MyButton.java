package core;

import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MyButton extends JButton{

	private Box box;

	private int speedX;
	private int speedY;
	
	public MyButton(JPanel panel, Box box, int x, int y,int width,int height, String text){
		this.setText(text);
		
		this.setBounds(new Rectangle(x, y, width, height));
		
		panel.add(this);
		
		speedX = 0;
		speedY = 0;
		
		this.box = box;
	}
	
	public void setSpeed(int x, int y){
		speedX = x;
		speedY = y;
	}
	
	public void update(){
		if(getModel().isPressed()){
			box.translate(speedX, speedY);
		}
	}
	
}
