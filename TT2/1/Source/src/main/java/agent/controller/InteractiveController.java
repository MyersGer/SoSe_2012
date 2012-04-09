package agent.controller;

import java.util.ArrayList;

import org.lwjgl.util.Point;
import org.openspaces.core.GigaSpace;

import agent.Agent;
import agent.Direction;
import world.AreaTuple;
import world.World;

public class InteractiveController extends Controller {

	boolean move;
	
	public InteractiveController(World w, Agent a, GigaSpace gs) {
		super(w, a, gs);
	}

	public void move() {
		
		ArrayList<AreaTuple> occupied = new ArrayList<AreaTuple>();
		for(Point p : world.getOccupiedAreas(agent)){
			occupied.add(new AreaTuple(world.getAreaIdForAreaCoord(p)));
		}
		
		ArrayList<AreaTuple> toBeRemoved = new ArrayList<AreaTuple>();
		for(AreaTuple at : blockedAreaList){
			if(!occupied.contains(at))
				toBeRemoved.add(at);
		}
		for(AreaTuple at : toBeRemoved){
			gigaSpace.write(at);
			blockedAreaList.remove(at);
		}
		
		if(move){			
			Point next = nextArea();

			if(next != null){ //agent is possibly able to move to new area
				Integer nextAreaId = world.getAreaIdForAreaCoord(next);
		
				if(nextAreaId != null && (!blockedAreaList.contains(new AreaTuple(nextAreaId)))){
					AreaTuple tt = this.gigaSpace.takeById(AreaTuple.class, nextAreaId);
					if(tt != null){
						agent.moveForward();
						blockedAreaList.add(tt);
					}
				
				}else{
					agent.moveForward();
				}
			}else{
				agent.moveForward();
			}
		}
		move = false;
	}
	
	public void proceed(){
		move = true;
	}
	
}
