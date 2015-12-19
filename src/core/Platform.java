package core;

import java.awt.Color;
import java.awt.Graphics2D;

public class Platform {

	int x;
	int y;
	int width;
	int height;
	int restitution;
	
	public Platform(int x,int y,int width, int height, int restitution){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.restitution = restitution;
	}
	
	public void update(Camera camera){
		
	}
	
	public void draw(Graphics2D g,Camera camera){
		g.setColor(new Color(255, 0, 0, 50));
		
		g.fillRect((int)(x-camera.getX()-width/2), (int)(500-y-camera.getY()-height/2), width, height);
		
		g.setColor(Color.WHITE);
	}
	
	//   /home/paulo/Desktop/testmap.map
}
