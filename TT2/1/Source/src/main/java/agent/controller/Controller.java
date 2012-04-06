package agent.controller;

import world.World;
import agent.Agent;

public abstract class Controller implements IController{
	
	protected World world;
	protected Agent agent;
	
	public Controller(World w, Agent a){
		world = w;
		agent = a;
	}
}
