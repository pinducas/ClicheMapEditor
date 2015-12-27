package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import managers.Manager;

@SuppressWarnings("serial")
public class Panel extends JPanel implements Runnable,KeyListener,MouseListener,MouseMotionListener {
	
	private Thread thread;
	private boolean running;
	
	private int FPS = 60;
	private long targetTime = 1000/FPS;
	
	private Manager manager;
	
	private JFrame frame;
	
	public Graphics2D gm;
	public Graphics2D gt;

	public BufferedImage map_image;
	public BufferedImage tiles_image;
	
	public Panel(JFrame frame,String []args){
		super();
		
		this.frame = frame;
		this.setDoubleBuffered(true);
		this.setLayout(null);
		
		setPreferredSize(new Dimension(1120, 600));
		
		map_image = new BufferedImage(780,500,BufferedImage.TYPE_INT_RGB);
		
		gm = (Graphics2D)map_image.getGraphics();
		gm.setBackground(Color.WHITE);	
		gm.setFont(new Font("Arial",Font.BOLD, 24));
		gm.setColor(Color.BLACK);
		
		tiles_image = new BufferedImage(240,240,BufferedImage.TYPE_INT_RGB);
		gt = (Graphics2D)tiles_image.getGraphics();
		gt.setBackground(Color.WHITE);	
		gt.setFont(new Font("Arial",Font.BOLD, 24));
		gt.setColor(Color.BLACK);
		
	
		manager = new Manager(this,args);
		
	}
	
	public JFrame getFrame(){
		return frame;
	}
	
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			setFocusable(true);
			requestFocus();
			running = true;
			thread.start();
		}
	}
	
	public void update(){
		manager.update();
	}
	
	public void draw(){
		gm.clearRect(1, 1, 778, 498);
		gt.clearRect(1, 1, 238, 238);
		manager.draw(gm,gt);
	}
	
	
	public void run() {
		long start;
		long elapsedTime;
		long wait;
		
		while(running){
			//loop
			start = System.nanoTime();
			update();
			
			if(manager.canDraw()){
				draw();
				
				Graphics g2 = getGraphics();
				g2.drawImage(map_image,10,10,780,500,null);
				g2.drawImage(tiles_image, 800, 268, 240, 240, null);
				g2.dispose();
			}
			else{
				manager.menuBar.repaint();
			}
			
			
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
	

	@Override
	public void keyPressed(KeyEvent e) {
		manager.keyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		manager.keyReleased(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		manager.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		manager.mouseReleased(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		manager.mouseDragged(e);		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		manager.mouseMoved(e);		
	}


}
