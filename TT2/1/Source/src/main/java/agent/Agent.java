package agent;



import java.util.Random;

import common.IUpdateable;

import org.lwjgl.util.Point;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


import agent.controller.IController;


public class Agent{
	
	private Point pos;
	private IController controller;
	private Direction direction = Direction.LEFT_TO_RIGHT;
	private double velocity = 0;
	private double progress = 0;
	private int width, height;
	private boolean isInteractive;
	private boolean isActive = false;
	
	
	public Agent(){
		
	}
	
	public Agent(boolean isInteractive){
		width = height = 33;
		Random rnd = new Random(System.nanoTime());
		velocity = rnd.nextDouble();
		if(velocity < 0.1)
			velocity = 0.1;
		if(velocity > 0.5)
			velocity = 1;
		
		this.isInteractive = isInteractive;
	}
	
	
	
	public boolean spawn(Point area, Point absPos, Direction dir){
		if(this.controller.reinit(area)){
			this.setPosition(absPos);
			this.setDirection(dir);
			return true;
		}
		return false;
	}
	
	public void moveForward(){
		if(progress >= 1){
			if(this.direction == Direction.BOTTOM_TO_TOP)
				this.pos.setY(pos.getY()-1);
			else if(this.direction == Direction.TOP_TO_BOTTOM)
				this.pos.setY(pos.getY()+1);
			else if(this.direction == Direction.LEFT_TO_RIGHT)
				this.pos.setX(pos.getX()+1);
			else if(this.direction == Direction.RIGHT_TO_LEFT)
				this.pos.setX(pos.getX()-1);
		}
	}
	
	public boolean isActive(){
		return this.isActive;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public int getWidth(){
		return this.width;
	}

	public Point getPosition(){
		return this.pos;
	}
	
	private void setDirection(Direction dir){
		this.direction = dir;
	}
	
	private void setPosition(Point absPos){
		this.pos = absPos;
	}
	
	public void setController(IController ctrl){
		this.controller = ctrl;
	}
	
	public IController getController(){
		return this.controller;
	}
	
	public Direction getDirection(){
		return direction;
	}
		
	public boolean isInteractive(){
		return isInteractive;
	}

	public void update() {
		if(progress > 1.1)
			progress = 0;
		this.progress+=velocity;
		this.controller.move();
	}
	
	public double getVelocity(){
		return velocity;
	}
	
	public void setVelocity(double velocity){
		this.velocity = velocity;
	}
}
