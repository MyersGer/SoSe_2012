package gui;

import org.lwjgl.util.Point;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import agent.Agent;
import agent.Direction;

public class VisualAgent {

	private Agent agent;
	private SpriteSheet spriteSheet = null;
	
	public VisualAgent(Agent agent) {
		this.agent = agent;

		try {
			if (agent.isInteractive())

				spriteSheet = new SpriteSheet(INTERACTIVE_AGENT_SPRITE, agent.getWidth(), agent.getHeight(), 0);
			else
				spriteSheet = new SpriteSheet(AUTONOMOUS_AGENT_SPRITE, agent.getWidth(), agent.getHeight(), 0);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Image getSprite() {
		Image subImage = null;
		if (agent.getDirection() == Direction.TOP_TO_BOTTOM)
			subImage = spriteSheet.getSubImage(0, 0);

		else if (agent.getDirection() == Direction.RIGHT_TO_LEFT)
			subImage = spriteSheet.getSubImage(0, 1);

		else if (agent.getDirection() == Direction.LEFT_TO_RIGHT)
			subImage = spriteSheet.getSubImage(0, 2);

		else if (agent.getDirection() == Direction.BOTTOM_TO_TOP)
			subImage = spriteSheet.getSubImage(0, 3);

		return subImage;
	}

	public Point getPosition() {
		return this.agent.getPosition();
	}

	public int getVisCorrectionY() {

		if (agent.getDirection() == Direction.RIGHT_TO_LEFT)
			return 22;
		else
			return 0;
	}

	public int getVisCorrectionX() {
		if (agent.getDirection() == Direction.TOP_TO_BOTTOM)
			return 17;
		else
			return 0;
	}

}
