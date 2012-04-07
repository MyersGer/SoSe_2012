package agent.controller;

import agent.Agent;
import world.World;

public class InteractiveController extends Controller {

	boolean move;
	
	public InteractiveController(World w, Agent a) {
		super(w, a);
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
