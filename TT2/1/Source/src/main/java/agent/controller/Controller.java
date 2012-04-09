package agent.controller;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import tuplespace.TupleSpace;

import org.lwjgl.util.Point;
import org.openspaces.core.GigaSpace;

import world.AreaTuple;
import world.World;
import agent.Agent;
import agent.Direction;

public abstract class Controller implements IController{
	
	protected World world;
	protected Agent agent;
	protected GigaSpace gigaSpace;
	protected ArrayList<AreaTuple> blockedAreaList = new ArrayList<AreaTuple>();
	
	public Controller(World w, Agent a, GigaSpace gs){
		world = w;
		agent = a;
		
	/*	TupleSpace tp = new TupleSpace("TileSpace");
		gigaSpace = tp.getGigaSpace();*/
		gigaSpace = gs;
	}
	
	public boolean reinit(Point area) {
		reset();
		Integer areaId = world.getAreaIdForAreaCoord(area);
		
		if(areaId != null){
			AreaTuple tt = gigaSpace.takeById(AreaTuple.class, areaId);
			
			if(tt != null){
				this.blockedAreaList.add(tt);
				return true;
			}
		}
		return false;
	}
	
	public void reset(){
		//free occupied tiles
		for(AreaTuple at:blockedAreaList){
			gigaSpace.write(at);
		}
		blockedAreaList.clear();
	}
	
	protected Point nextArea(){
		Point nextArea = null;
		
		ArrayList<Point> areas = world.getOccupiedAreas(agent);
		
		
		if(areas.size()<2){ //currently occupies one tile, otherwise agent is in movement
			Point area1 = areas.get(0);
			if(agent.getDirection() == Direction.BOTTOM_TO_TOP)
				nextArea = new Point(area1.getX(), area1.getY()-1);
			else if(agent.getDirection() == Direction.TOP_TO_BOTTOM)
				nextArea = new Point(area1.getX(), area1.getY()+1);
			else if(agent.getDirection() == Direction.LEFT_TO_RIGHT)
				nextArea = new Point(area1.getX()+1, area1.getY());
			else if(agent.getDirection() == Direction.RIGHT_TO_LEFT)
				nextArea = new Point(area1.getX()-1, area1.getY());
		}
		
		return nextArea;
	}
}
