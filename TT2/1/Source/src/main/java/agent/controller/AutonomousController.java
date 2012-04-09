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
				
				if(world.getOccupiedAreas(agent).size() == 1 && blockedAreaQueue.size() > 0)
					gigaSpace.write(blockedAreaQueue.poll());
				
				if(this.blockedAreaQueue.contains(new AreaTuple(nextAreaId))) //if agent already owns area just move
					agent.moveForward();
				else{
					AreaTuple tt = this.gigaSpace.takeById(AreaTuple.class, nextAreaId);
					if(tt != null){
						agent.moveForward();
						blockedAreaQueue.add(tt);

						if(world.getOccupiedAreas(agent).size() == 1)
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
}
