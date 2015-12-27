package elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import tools.Camera;

public class Platform {

	public int x;
	public int y;
	public int width;
	public int height;
	public int friction;
	public int id;
	
	public Platform(int x,int y,int width, int height, int friction,int id){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.friction = friction;
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public void update(Camera camera){
		
	}
	
	public Rectangle getRect(Camera camera){
		return new Rectangle((int)(x-camera.getX()-width/2),(int)(500-y-camera.getY()-height/2),
				width,height);
	}
	
	public void draw(Graphics2D g,Camera camera){
		g.setColor(new Color(255, 0, 0, 150));
		
		g.fillRect((int)(x-camera.getX()-width/2), (int)(500-y-camera.getY()-height/2), width, height);
		
		g.setColor(Color.WHITE);
	}
	
	//   /home/paulo/Desktop/testmap.map
}
