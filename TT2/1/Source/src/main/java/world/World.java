package world;
import java.util.ArrayList;

import org.newdawn.slick.tiled.TiledMap;

import character.Character;


public class World {
	
	private TiledMap map;
	private ArrayList<Character> characterList = new ArrayList<Character>();
	private Character player;
	
	public World(TiledMap map){
		this.map = map;
	}
	
	
}
