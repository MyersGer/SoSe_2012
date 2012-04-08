package agent.controller;

import java.util.concurrent.LinkedBlockingQueue;

import tuplespace.TupleSpace;

import org.lwjgl.util.Point;
import org.openspaces.core.GigaSpace;

import world.TileTuple;
import world.World;
import agent.Agent;

public abstract class Controller implements IController{
	
	protected World world;
	protected Agent agent;
	protected GigaSpace gigaSpace;
	protected LinkedBlockingQueue<TileTuple> blockedTileQueue = new LinkedBlockingQueue();
	
	public Controller(World w, Agent a, GigaSpace gs){
		world = w;
		agent = a;
		
	/*	TupleSpace tp = new TupleSpace("TileSpace");
		gigaSpace = tp.getGigaSpace();*/
		gigaSpace = gs;
	}
	
	public boolean reinit(Point p) {
		reset();
		Integer tileId = world.getTileIdForTileCoord(p);
		
		if(tileId != null){
			TileTuple tt = gigaSpace.readById(TileTuple.class, tileId);
			
			if(tt != null){
				this.blockedTileQueue.add(tt);
				return true;
			}
		}
		return false;
	}
	
	public void reset(){
		//free occupied tiles
		while(!this.blockedTileQueue.isEmpty()){
			gigaSpace.write(blockedTileQueue.poll());
		}
	}
}
