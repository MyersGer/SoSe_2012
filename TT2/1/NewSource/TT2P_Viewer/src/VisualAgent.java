
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.lwjgl.util.Point;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import datatypes.Area;
import datatypes.Car;
import datatypes.Direction;

public class VisualAgent {

	private boolean isInteractive;
	private Direction direction;
	private Point postition;
	
	private SpriteSheet spriteSheet = null;
	private static final Integer SPRITE_DIMENSION = 33;
	private final static String AUTONOMOUS_AGENT_SPRITE = "maps/car_npc.png";
	private final static String INTERACTIVE_AGENT_SPRITE = "maps/car_pc.png";
	private static InputStream ins;
	private static Image image;
	
	static{
		try {
			ins = new FileInputStream(new File(AUTONOMOUS_AGENT_SPRITE));
			image = new Image(ins, "NPC_CAR", false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private final static Integer TILE_HEIGHT = 48;
	private final static Integer TILE_WIDTH = 48;

	public VisualAgent(boolean isInteractive, Direction direction, Point postition) {
		this.isInteractive = isInteractive;
		this.direction = direction;
		this.postition = postition;
		
		try {
			if (isInteractive){
				spriteSheet = new SpriteSheet(VisualAgent.INTERACTIVE_AGENT_SPRITE, SPRITE_DIMENSION, SPRITE_DIMENSION, 0);
			}else
				{
//				spriteSheet = new SpriteSheet(VisualAgent.AUTONOMOUS_AGENT_SPRITE, SPRITE_DIMENSION, SPRITE_DIMENSION, 0);
				spriteSheet = new SpriteSheet(image, SPRITE_DIMENSION, SPRITE_DIMENSION, 0);
				}
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Image getSprite() {
		Image subImage = null;
		
		switch (direction) {
		case TOP_TO_BOTTOM:
			subImage = spriteSheet.getSubImage(0, 0);
			break;
		case RIGHT_TO_LEFT:
			subImage = spriteSheet.getSubImage(0, 1);
			break;
		case LEFT_TO_RIGHT:
			subImage = spriteSheet.getSubImage(0, 2);
			break;
		case BOTTOM_TO_TOP:
			subImage = spriteSheet.getSubImage(0, 3);
			break;
		default:
			break;
		}
		
		return subImage;
	}

	public Point getPosition() {
		return new Point(postition.getX() * TILE_WIDTH + getVisCorrectionX(), postition.getY() * TILE_HEIGHT + getVisCorrectionY());
	}

	public int getVisCorrectionY() {

		if (direction == Direction.RIGHT_TO_LEFT)
			return 22;
		else
			return 0;
	}

	public int getVisCorrectionX() {
		if (direction == Direction.TOP_TO_BOTTOM)
			return 17;
		else
			return 0;
	}

}
