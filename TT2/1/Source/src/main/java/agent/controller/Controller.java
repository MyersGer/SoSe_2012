package agent.controller;

import java.util.concurrent.LinkedBlockingQueue;

import tuplespace.TupleSpace;

import org.lwjgl.util.Point;
import org.openspaces.core.GigaSpace;

import world.AreaTuple;
import world.World;
import agent.Agent;

public abstract class Controller implements IController{
	
	protected World world;
	protected Agent agent;
	protected GigaSpace gigaSpace;
	protected LinkedBlockingQueue<AreaTuple> blockedAreaQueue = new LinkedBlockingQueue<AreaTuple>();
	
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
			AreaTuple tt = gigaSpace.readById(AreaTuple.class, areaId);
			
			if(tt != null){
				this.blockedAreaQueue.add(tt);
				return true;
			}
		}
		return false;
	}
	
	public void reset(){
		//free occupied tiles
		while(!this.blockedAreaQueue.isEmpty()){
			gigaSpace.write(blockedAreaQueue.poll());
		}
	}
}
