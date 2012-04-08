package agent.controller;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.util.Point;
import org.openspaces.core.GigaSpace;

import agent.Agent;
import agent.Direction;
import world.TileTuple;
import world.World;

public class AutonomousController extends Controller {
	
	public AutonomousController(World w, Agent a, GigaSpace gs) {
		super(w, a, gs);
	}

	public void move() {
		Point next = nextTile();
		if(next != null){
			//grab the tile out of the tuplespace
			Integer nextTileId = world.getTileIdForTileCoord(next);
			if(nextTileId != null){ //check if agent moves out of map
				TileTuple tt = this.gigaSpace.readById(TileTuple.class, nextTileId);
				if(tt != null){
					agent.moveForward();
					blockedTileQueue.add(tt);
					gigaSpace.write(blockedTileQueue.poll());
				}
				
			}else{
				if(!blockedTileQueue.isEmpty()){
					gigaSpace.write(blockedTileQueue.poll()); //if out of map, clear write last tile back
				}
				agent.moveForward();
			}
		}else{ //we are in movement
			agent.moveForward();
		}
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
