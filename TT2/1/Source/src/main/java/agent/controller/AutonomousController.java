package agent.controller;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.util.Point;
import org.openspaces.core.GigaSpace;

import agent.Agent;
import agent.Direction;
import world.AreaTuple;
import world.World;

public class AutonomousController extends Controller {
	
	public AutonomousController(World w, Agent a, GigaSpace gs) {
		super(w, a, gs);
	}

	public void move() {
		Point next = nextArea();
		
		if(next != null){
			//grab the tile out of the tuplespace
			Integer nextAreaId = world.getAreaIdForAreaCoord(next);
			if(nextAreaId != null){ //check if agent moves out of map
				if(this.blockedAreaQueue.contains(new AreaTuple(nextAreaId))) //if agent already owns area just move
					agent.moveForward();
				else{
					AreaTuple tt = this.gigaSpace.takeById(AreaTuple.class, nextAreaId);
					if(tt != null){
						agent.moveForward();
						blockedAreaQueue.add(tt);
						gigaSpace.write(blockedAreaQueue.poll());
					}
				}
				
			}else{
				if(!blockedAreaQueue.isEmpty()){
					gigaSpace.write(blockedAreaQueue.poll()); //if out of map, clear write last tile back
				}
				agent.moveForward();
			}
		}else{ //we are in movement
			agent.moveForward();
		}
	}
	
	private Point nextArea(){
		Point nextTile = null;
		
		ArrayList<Point> tiles = world.getOccupiedAreas(agent);
		
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
