package core;

public class Box {

	private int x;
	private int y;
	private int width;
	private int height;
	
	public Box(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public void setWidth(int width){
		this.width = width;
	}
	public void setHeight(int height){
		this.height = height;
	}
	public void translate(int x,int y){
		this.x += x;
		this.y += y;
		if(this.x > 780 - width)this.x = 780-width;
		if(this.y > 500 - height)this.y = 500-height;
		if(this.x < 10)this.x = 10;
		if(this.y < 10)this.y = 10;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	
}
