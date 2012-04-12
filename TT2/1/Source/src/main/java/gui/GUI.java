package gui;
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


import agent.Agent;
import agent.Direction;
import agent.controller.IController;
import agent.controller.InteractiveController;


import world.World;

public class GUI extends BasicGame {

	private String map_file, mapgfx_location;
	private TiledMap map;
	private World world;
	

	public GUI(String map_file, String mapgfx_location) {
		super("TupleSpace");
		
		this.map_file = map_file;
		this.mapgfx_location = mapgfx_location;
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		
		map = new TiledMap(map_file, mapgfx_location);
		world = new World(map, 20);
		gc.setTargetFrameRate(50);
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		//input processing
		Input in = gc.getInput();
		
		if (in.isKeyDown(Input.KEY_UP)) {
			//TODO: in tuplespace schreiben, dass agent sich bewegen soll
		} else if (in.isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}
		
		world.update();
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {

		ArrayList<Agent> agents = world.getAgentList();

		map.render(0, 0, 0, 0, map.getWidth(), map.getHeight());
		
		for(Agent agent: agents){
			VisualAgent visag = new VisualAgent(agent, "src/main/resources/objects/car_npc.png", "src/main/resources/objects/car_pc.png");
			visag.getSprite().draw(visag.getPosition().getX()+visag.getVisCorrectionX(),
									agent.getPosition().getY()+visag.getVisCorrectionY(),
									visag.getSprite().getWidth(),
									visag.getSprite().getHeight());
		}
	}

	@Override
	public boolean closeRequested() {
		return false;
	}
	
	private void readAgents(){
		//read Agents from TupleSpace
	}
	
	private void movePlayer(){
		//TODO: write move message to TupleSpace
	}

}