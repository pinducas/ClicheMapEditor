package elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tools.Loader;

@SuppressWarnings("serial")
public class ColorPickerPanel extends JPanel implements MouseListener,ActionListener{

	private BufferedImage pallete;
	private Color currentColor;
	
	private JTextField []rgb;
	
	private JLabel redLabel;
	private JLabel greenLabel;
	private JLabel blueLabel;

	
	public ColorPickerPanel(){		
		setLayout(new GroupLayout(this));
		
		currentColor = new Color(0, 0, 0);
		Loader l = new Loader();
		pallete = l.loadImage("color.png");
		
		requestFocus();
		addMouseListener(this);
		
		pallete = getScaledImage(pallete, 512, 512);
	
		redLabel = new JLabel("RED: ");
		redLabel.setBounds(new Rectangle(250,525,300,100));
		add(redLabel);
		greenLabel = new JLabel("GREEN: ");
		greenLabel.setBounds(new Rectangle(400,525,300,100));
		add(greenLabel);
		blueLabel = new JLabel("BLUE: ");
		blueLabel.setBounds(new Rectangle(575,525,300,100));
		add(blueLabel);
		
		rgb = new JTextField[3];
		rgb[0] = new JTextField(3);
		rgb[1] = new JTextField(3);
		rgb[2] = new JTextField(3);
		for(int i = 0; i <3; i++){
			rgb[i].setText("0");
			rgb[i].setBounds(new Rectangle(290+i*170,565,40,20));
			rgb[i].addActionListener(this);	
			add(rgb[i]);

		}
		
		
		this.setPreferredSize(new Dimension(800, 620));
	}
	
	public void paint(java.awt.Graphics g) {
		super.paint(g);
		g.drawImage(pallete, 128, 10, null);
	
		g.setColor(currentColor);
		g.fillRect(128, 530, 100, 100);
		
		
		redLabel.repaint();
		
	}
	
	public void setColor(Color color){
		this.currentColor = color;
	}
	
	public Color getColor(){
		return currentColor;
	}
	
	
	//NOT MY CODE
	private BufferedImage getScaledImage(BufferedImage srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX()-128;
		int y = e.getY()-10;
		
		if(x< 0)x = 0;
		if(y < 0)y =0;
		
		if(x > 511)x = 511;
		if(y > 511)y = 511;
		
		
		repaint();
		
		currentColor = new Color(pallete.getRGB(x,y));
		rgb[0].setText(""+currentColor.getRed());
		rgb[1].setText(""+currentColor.getGreen());
		rgb[2].setText(""+currentColor.getBlue());		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		try{
			int r = Integer.parseInt(rgb[0].getText());
			int g = Integer.parseInt(rgb[1].getText());
			int b = Integer.parseInt(rgb[2].getText());	
			System.out.println("WHOT");

			if(r < 0)r = 0;
			if(g < 0)g = 0;
			if(b < 0)b = 0;
			if(r > 255)r = 255;
			if(g > 255)g = 255;
			if(b > 255)b = 255;
			
			currentColor = new Color(r,g,b);
			repaint();
		}catch(Exception ee){
			
		}
	}
	
}
