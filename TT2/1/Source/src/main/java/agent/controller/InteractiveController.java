package agent.controller;

import org.lwjgl.util.Point;
import org.openspaces.core.GigaSpace;

import agent.Agent;
import world.World;

public class InteractiveController extends Controller {

	boolean move;
	
	public InteractiveController(World w, Agent a, GigaSpace gs) {
		super(w, a, gs);
	}

	public void move() {		
		if(move){
			agent.moveForward();
		}
		move = false;
	}
	
	public void proceed(){
		move = true;
	}
}
