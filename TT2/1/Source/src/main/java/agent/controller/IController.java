package agent.controller;

import org.lwjgl.util.Point;

public interface IController {
	public void move();
	public boolean reinit(Point area);
	public void reset();
}
