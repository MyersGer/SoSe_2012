package agent;



import org.lwjgl.util.Point;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import basic.IUpdateable;

import agent.controller.IController;


public class Agent implements IUpdateable{
	
	private SpriteSheet spriteSheet = null;
	private Point pos;
	private Point tile;
	private IController controller;
	private Direction direction = Direction.LEFT_TO_RIGHT;
	
	public Agent(){
		
	}
	
	public Agent(String spriteSheet){
		try {
			this.spriteSheet = new SpriteSheet(spriteSheet, 33, 33);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/*
	public void setSpriteSheet(String spriteSheet){
		this.spriteSheet = new SpriteSheet(spriteSheet;
	}
	
	public String getSpriteSheet(){
		return this.spriteSheet;
	}*/
	
	public void setDirection(Direction dir){
		this.direction = dir;
	}
	
	public void setTile(Point tile){
		this.tile = tile;
	}
	
	public Point getTilePosition(){
		return this.tile;
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

		if(this.direction == Direction.RIGHT_TO_LEFT)
			subImage = spriteSheet.getSubImage(0, 1);
		
		if(this.direction == Direction.LEFT_TO_RIGHT)
			subImage = spriteSheet.getSubImage(0, 2);
		
		if(this.direction == Direction.BOTTOM_TO_TOP)
			subImage = spriteSheet.getSubImage(0, 3);
		
		return subImage;
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}
}
