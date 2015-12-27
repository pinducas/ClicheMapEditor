package elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import tools.Camera;

public class PointLight {

	public int x;
	public int y;
	public int radius;
	public int id;
	public Color color;
	private boolean active;
	
	public PointLight(int x,int y,int radius,int id,Color color){
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.id = id;
		this.color = color;
		active = false;
	}
	
	public int getId(){
		return id;
	}
	
	public void update(Camera camera){
		if(new Rectangle((int)(x-radius),(int)(500-y-radius),
				radius*2,radius*2).intersects(new Rectangle((int)camera.getX(),(int)camera.getY(),(int)camera.getW(),(int)camera.getH()))){
		 active = true;
		}
		else active = false;
	}
	
	public Rectangle getRect(Camera camera){
		return new Rectangle((int)(x-camera.getX()-radius),(int)(500-y-camera.getY()-radius),
				radius*2,radius*2);
	}
	
	public void draw(Graphics2D g,Camera camera){
		if(!active)
		for(int i = 1; i < 5; i++){
			int r = radius - radius/i;
			g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),255/i));
			
			g.fillOval((int)(x-camera.getX()-r), (int)(500-y-camera.getY()-r), r*2, r*2);
		}
		g.setColor(Color.WHITE);
	}
	
	//  
	
}
