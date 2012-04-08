package agent.controller;

import tuplespace.TupleSpace;
import org.openspaces.core.GigaSpace;

import world.World;
import agent.Agent;

public abstract class Controller implements IController{
	
	protected World world;
	protected Agent agent;
	protected GigaSpace gigaSpace;
	
	public Controller(World w, Agent a){
		world = w;
		agent = a;
		
		TupleSpace tp = new TupleSpace("TileSpace");
		gigaSpace = tp.getGigaSpace();
	}
}
