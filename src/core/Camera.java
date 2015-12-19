package core;

public class Camera {

	private float x;
	private float y;
	
	private float width;
	private float height;
	
	public Camera(float x, float y, float widht,float height){
		this.x = x;
		this.y = y;
		this.width = widht;
		this.height = height;
	}
	
	public float getW(){
		return width;
	}
	
	public float getH(){
		return height;
	}
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	
	public void translate(float x, float y){
		this.x += x;
		this.y += y;
	}
	
	public void update(){
		
		
	}
	
}
