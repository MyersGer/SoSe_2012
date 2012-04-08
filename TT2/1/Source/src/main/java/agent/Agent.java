package agent;



import java.util.Random;

import org.lwjgl.util.Point;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import basic.IUpdateable;

import agent.controller.IController;


public class Agent implements IUpdateable{
	
	private SpriteSheet spriteSheet = null;
	private Point pos;
	private IController controller;
	private Direction direction = Direction.LEFT_TO_RIGHT;
	private double velocity = 0;
	private double progress = 0;
	private int width, height;
	
	private int visCorrectionFaktor = 15;
	
	public Agent(String spriteSheet){
		width = height = 33;
		Random rnd = new Random(System.nanoTime());
		velocity = rnd.nextDouble();
		if(velocity < 0.1)
			velocity = 0.1;
		if(velocity > 0.5)
			velocity = 1;
		
		try {
			this.spriteSheet = new SpriteSheet(spriteSheet, width, height);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
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

	public Point getPosition(){
		return this.pos;
	}
	
	public int getVisCorrectionY(){
		if(direction == Direction.RIGHT_TO_LEFT)
			return 22;
		else
			return 0;
	}
	
	public int getVisCorrectionX(){
		if(direction == Direction.TOP_TO_BOTTOM)
			return 17;
		else
			return 0;
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
		
	public Image getSprite(){
		Image subImage = null;
		if(this.direction == Direction.TOP_TO_BOTTOM)
			subImage = spriteSheet.getSubImage(0, 0);

		else if(this.direction == Direction.RIGHT_TO_LEFT)
			subImage = spriteSheet.getSubImage(0, 1);
		
		else if(this.direction == Direction.LEFT_TO_RIGHT)
			subImage = spriteSheet.getSubImage(0, 2);
		
		else if(this.direction == Direction.BOTTOM_TO_TOP)
			subImage = spriteSheet.getSubImage(0, 3);
		
		return subImage;
	}

	public double getProceeding(){
		return progress;
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
