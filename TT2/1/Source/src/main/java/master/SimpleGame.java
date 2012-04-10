package master;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.lwjgl.util.Point;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import common.map.Area;
import common.map.AreaDirection;
import common.map.AreaSide;
import common.world.OccupationAreaTuple;


import agent.Agent;
import agent.Direction;
import agent.controller.AutonomousController;
import agent.controller.IController;
import agent.controller.InteractiveController;


import world.World;

public class SimpleGame extends BasicGame {

	private String map_file, mapgfx_location;
	private TiledMap map;
	private World world;
	

	public SimpleGame(String map_file, String mapgfx_location) {
		super("TupleSpace");
		
		this.map_file = map_file;
		this.mapgfx_location = mapgfx_location;
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		
		map = new TiledMap(map_file, mapgfx_location);
		gc.setTargetFrameRate(50);
		
		int streetLayerIndex = map.getLayerIndex("strassennetz");
		
		int areaId = 0;
		//collect tiles where cars can be spawned, create mapping tileID->XY, init tuplespace 
		for(int y=0; y<map.getHeight(); y++){
			for(int x=0; x<map.getWidth(); x++){
				int tileId = map.getTileId(x, y, streetLayerIndex);
				
				//check for spawn property
				String propSpawn = map.getTileProperty(tileId, "spawn", "false");
				boolean spawn = false;
				if(propSpawn.equals("true")){
					spawn = true;
				}
				
				AreaDirection dir = null;
				String propDirection = map.getTileProperty(tileId, "direction", "horizontal");
				if(propDirection.equals("horizontal"))
					dir = AreaDirection.HORIZONTAL;
				else if(propDirection.equals("vertical"))
					dir = AreaDirection.VERTICAL;
				
				AreaSide side = null;
				String propSide = map.getTileProperty(tileId, "side", "left");
				if(propSide.equals("left"))
					side = AreaSide.LEFT;
				else if(propSide.equals("right"))
					side = AreaSide.RIGHT;
				
				Area area = new Area(areaId, new Point(x, y), dir, side, spawn);
				//TODO: write Area to tuplespace
				
				areaId++;
			}
		}
		
		gc.exit();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
	
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {

	}

	@Override
	public boolean closeRequested() {
		return false;
	}

}