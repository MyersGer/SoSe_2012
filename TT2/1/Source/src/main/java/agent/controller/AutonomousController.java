package agent.controller;

import java.util.ArrayList;

import org.lwjgl.util.Point;

import agent.Agent;
import agent.Direction;
import world.World;

public class AutonomousController extends Controller {

	
	public AutonomousController(World w, Agent a) {
		super(w, a);
	}

	public void move() {
		
		agent.moveForward();
	}
	
	private Point nextTile(){
		Point nextTile = null;
		
		ArrayList<Point> tiles = world.getOccupiedTiles(agent);
		
		Point tile1 = tiles.get(0);
		Point tile2 = tiles.get(1);
		
		if(tile1.equals(tile2)){ //currently occupies one tile, otherwise agent is in movement
			if(agent.getDirection() == Direction.BOTTOM_TO_TOP)
				nextTile = new Point(tile1.getX(), tile1.getY()-1);
			else if(agent.getDirection() == Direction.TOP_TO_BOTTOM)
				nextTile = new Point(tile1.getX(), tile1.getY()+1);
			else if(agent.getDirection() == Direction.LEFT_TO_RIGHT)
				nextTile = new Point(tile1.getX(), tile1.getY()+1);
			else if(agent.getDirection() == Direction.RIGHT_TO_LEFT)
				nextTile = new Point(tile1.getX(), tile1.getY()-1);
		}
		
		return nextTile;
	}
}
