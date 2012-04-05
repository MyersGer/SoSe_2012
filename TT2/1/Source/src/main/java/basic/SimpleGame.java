package basic;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import world.World;

public class SimpleGame extends BasicGame {

	private String map_file, mapgfx_location;
	private TiledMap map;
	private World world;
	
	private long beforeTime, afterTime, timeDiff, sleepTime, overSleepTime = 0;



	public SimpleGame(String map_file, String mapgfx_location) {
		super("TupleSpace");
		
		this.map_file = map_file;
		this.mapgfx_location = mapgfx_location;
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		
		map = new TiledMap(map_file, mapgfx_location);
		world = new World(map);
		gc.setTargetFrameRate(50);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
	
	}

	private void processInput(Input in) {
		if (in.isKeyDown(Input.KEY_DOWN)) {
			
		} else if (in.isKeyDown(Input.KEY_UP)) {

		} else if (in.isKeyDown(Input.KEY_LEFT)) {

		} else if (in.isKeyDown(Input.KEY_RIGHT)) {

		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {

		int tileWidth = map.getTileWidth();
		int tileHeight = map.getTileHeight();
		
		map.render(0, 0, 0, 0, map.getHeight(), map.getWidth());
		

	}

	@Override
	public boolean closeRequested() {
		
		return false;
	}

}