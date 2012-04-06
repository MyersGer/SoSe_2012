package agent.controller;

import agent.Agent;
import world.World;

public class AutonomousController extends Controller {

	public AutonomousController(World w, Agent a) {
		super(w, a);
	}

	public void move() {
		agent.moveForward();
	}
	
}
