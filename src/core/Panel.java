package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel implements Runnable {
		
	private Thread thread;
	private boolean running;
	
	private int FPS = 60;
	private long targetTime = 1000/FPS;
	
	private Box box;
	
	private MyButton[]buttons;
	
	private TextBox textBox;
	
	public Panel(){
		super();
		
		this.setLayout(null);
		
		setPreferredSize(new Dimension(800, 600));
		
		box = new Box(20,20,100,100);
		
		buttons = new MyButton[4];
		buttons[0] = new MyButton(this, box, 40, 540, 100, 40, "LEFT");
		buttons[1] = new MyButton(this, box, 160, 540, 100, 40, "DOWN");
		buttons[2] = new MyButton(this, box, 280, 540, 100, 40, "UP");
		buttons[3] = new MyButton(this, box, 400, 540, 100, 40, "RIGHT");

		buttons[0].setSpeed(-10, 0);
		buttons[1].setSpeed(0, 10);
		buttons[2].setSpeed(0, -10);
		buttons[3].setSpeed(10, 0);
		
		JLabel label = new JLabel();
		label.setText("Size: ");
		label.setBounds(560,510,100,100);
			
		this.add(label);
		
		textBox = new TextBox(this, box, 600, 540, 150, 40, ""+box.getWidth());
		
	}
	
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			running = true;
			thread.start();
		}
	}
	
	public void update(){
		for(MyButton b:buttons)
			b.update();
		textBox.update();
	}
	
	public void draw(){
		Graphics g2 = getGraphics();
		
		g2.setColor(Color.WHITE);
		g2.fillRect(10, 10, 780, 500);
		g2.setColor(new Color(255, 0, 0));
		g2.fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
		
		g2.dispose();
	}
	
	
	public void run() {
		long start;
		long elapsedTime;
		long wait;
		
		while(running){
			//loop
			start = System.nanoTime();
			update();
			draw();
			
			elapsedTime= System.nanoTime()-start;
			wait = targetTime-elapsedTime/1000000;
			if(wait<0)
				wait=5;
			try{
				Thread.sleep(wait);
			}catch(Exception e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Deu ruim atualizando o thread");
				System.exit(0);
			}
			
		}
		
		
	}


}
