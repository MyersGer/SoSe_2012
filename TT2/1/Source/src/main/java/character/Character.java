package character;

import org.lwjgl.util.Point;

import character.controller.IController;

public abstract class Character {
	
	private String spriteSheet = null;
	private Point pos;
	
	public Character(){
		
	}
	
	public Character(String spriteSheet){
		
	}	
	
	public void setSpriteSheet(String spriteSheet){
		this.spriteSheet = spriteSheet;
	}
	
	public String getSpriteSheet(){
		return this.spriteSheet;
	}
	
	public Point getPosition(){
		return this.pos;
	}
	
	public void setController(IController ctrl){
		
	}
	
}
