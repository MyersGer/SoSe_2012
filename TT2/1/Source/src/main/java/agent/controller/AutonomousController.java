package agent.controller;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.util.Point;
import org.openspaces.core.GigaSpace;

import common.world.OccupationAreaTuple;

import agent.Agent;
import agent.Direction;
import world.World;

public class AutonomousController extends Controller {
	
	public AutonomousController(World w, Agent a, GigaSpace gs) {
		super(w, a, gs);
	}

	public void move() {
		ArrayList<OccupationAreaTuple> occupied = new ArrayList<OccupationAreaTuple>();
		for(Point p : world.getOccupiedAreas(agent)){
			occupied.add(new OccupationAreaTuple(world.getAreaIdForAreaCoord(p)));
		}
		
		ArrayList<OccupationAreaTuple> toBeRemoved = new ArrayList<OccupationAreaTuple>();
		for(OccupationAreaTuple at : blockedAreaList){
			if(!occupied.contains(at))
				toBeRemoved.add(at);
		}
		for(OccupationAreaTuple at : toBeRemoved){
			gigaSpace.write(at);
			blockedAreaList.remove(at);
		}
			
		Point next = nextArea();
		
		if(next != null){ //agent is possibly able to move to new area
			Integer nextAreaId = world.getAreaIdForAreaCoord(next);
			
			if(nextAreaId != null && (!blockedAreaList.contains(new OccupationAreaTuple(nextAreaId)))){
				OccupationAreaTuple tt = this.gigaSpace.takeById(OccupationAreaTuple.class, nextAreaId);
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
}
