package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Platform {

	int x;
	int y;
	int width;
	int height;
	int friction;
	
	public Platform(int x,int y,int width, int height, int friction){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.friction = friction;
	}
	
	public void update(Camera camera){
		
	}
	
	public Rectangle getRect(Camera camera){
		return new Rectangle((int)(x-camera.getX()-width/2),(int)(500-y-camera.getY()-height/2),
				width,height);
	}
	
	public void draw(Graphics2D g,Camera camera){
		g.setColor(new Color(255, 0, 0, 50));
		
		g.fillRect((int)(x-camera.getX()-width/2), (int)(500-y-camera.getY()-height/2), width, height);
		
		g.setColor(Color.WHITE);
	}
	
	//   /home/paulo/Desktop/testmap.map
}
