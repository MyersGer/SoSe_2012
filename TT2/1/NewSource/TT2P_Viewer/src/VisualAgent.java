
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

	private Car car;
	private Area area;
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

	public VisualAgent(Car car, Area area) {
		this.car = car;
		this.area = area;
		try {
			if (car.isInteractive()){
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
		if (car.getDirection() == Direction.TOP_TO_BOTTOM)
			subImage = spriteSheet.getSubImage(0, 0);

		else if (car.getDirection() == Direction.RIGHT_TO_LEFT)
			subImage = spriteSheet.getSubImage(0, 1);

		else if (car.getDirection() == Direction.LEFT_TO_RIGHT)
			subImage = spriteSheet.getSubImage(0, 2);

		else if (car.getDirection() == Direction.BOTTOM_TO_TOP)
			subImage = spriteSheet.getSubImage(0, 3);

		return subImage;
	}

	public Point getPosition() {
		return new Point(this.area.getPos().getX() * TILE_WIDTH + getVisCorrectionX(), this.area.getPos().getY() * TILE_HEIGHT + getVisCorrectionY());
	}

	public int getVisCorrectionY() {

		if (car.getDirection() == Direction.RIGHT_TO_LEFT)
			return 22;
		else
			return 0;
	}

	public int getVisCorrectionX() {
		if (car.getDirection() == Direction.TOP_TO_BOTTOM)
			return 17;
		else
			return 0;
	}

}
