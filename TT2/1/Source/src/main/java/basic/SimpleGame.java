package basic;
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
		world = new World(map, 20);
		gc.setTargetFrameRate(50);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		//input processing
		agent.Agent player = world.getPlayer();
		InteractiveController controller = (InteractiveController)player.getController();
		
		Input in = gc.getInput();
		
		if (in.isKeyDown(Input.KEY_DOWN)) {
			if(player.getDirection() == Direction.TOP_TO_BOTTOM)
				controller.proceed();
		} else if (in.isKeyDown(Input.KEY_UP)) {
			if(player.getDirection() == Direction.BOTTOM_TO_TOP)
				controller.proceed();
		} else if (in.isKeyDown(Input.KEY_LEFT)) {
			if(player.getDirection() == Direction.RIGHT_TO_LEFT)
				controller.proceed();
		} else if (in.isKeyDown(Input.KEY_RIGHT)) {
			if(player.getDirection() == Direction.LEFT_TO_RIGHT)
				controller.proceed();
		} else if (in.isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}
		else if (in.isKeyPressed(Input.KEY_W)){
			player.setVelocity(player.getVelocity()-0.1);
		}
		else if (in.isKeyPressed(Input.KEY_Q)){
			player.setVelocity(player.getVelocity()+0.1);
		}
		
		world.update();
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {

		//stuff we need to know
		int tileWidth = map.getTileWidth();
		int tileHeight = map.getTileHeight();
		ArrayList<Agent> agents = world.getAgentList();

		//middle of the map
        int centerx = (gc.getWidth() / tileWidth) / 2;
        int centery = (gc.getHeight() / tileHeight) / 2;

		
		map.render(0, 0, 0, 0, map.getWidth(), map.getHeight());
		
		for(Agent agent: agents){
			Image sprite = agent.getSprite();
			sprite.draw(agent.getPosition().getX()+agent.getVisCorrectionX(), agent.getPosition().getY()+agent.getVisCorrectionY(), sprite.getWidth(), sprite.getHeight());
		}
	}

	@Override
	public boolean closeRequested() {
		return false;
	}

}