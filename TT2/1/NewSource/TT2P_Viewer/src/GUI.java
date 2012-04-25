import java.util.ArrayList;

import org.apache.log4j.jmx.Agent;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.openspaces.core.GigaSpace;

import com.j_spaces.core.client.SQLQuery;

import datatypes.Area;
import datatypes.Car;

public class GUI extends BasicGame {

	private String map_file, mapgfx_location;
	private TiledMap map;
	private GigaSpace gigaSpace;

	public GUI(String map_file, String mapgfx_location) {
		super("TupleSpace");

		gigaSpace = DataGridConnectionUtility.getSpace("space", 1, 0);
		this.map_file = map_file;
		this.mapgfx_location = mapgfx_location;
	}

	@Override
	public void init(GameContainer gc) throws SlickException {

		map = new TiledMap(map_file, mapgfx_location);
		gc.setTargetFrameRate(50);

	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// input processing
		Input in = gc.getInput();

		if (in.isKeyDown(Input.KEY_UP)) {
			// TODO: in tuplespace schreiben, dass agent sich bewegen soll
		} else if (in.isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}

	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		map.render(0, 0, 0, 0, map.getWidth(), map.getHeight());

		SQLQuery<Area> occupiedAreaQuery = new SQLQuery<Area>(Area.class, "spawn=TRUE and occupiedById != '" + Area.EMPTY + "'");
		Area[] occupiedPoints;
		occupiedPoints = gigaSpace.readMultiple(occupiedAreaQuery);
		for (Area occupiedPoint : occupiedPoints) {
			SQLQuery<Car> carQuery = new SQLQuery<Car>(Car.class, "id = '" + occupiedPoint.getOccupiedById() + "'");
			Car car = gigaSpace.read(carQuery);
			VisualAgent visag = new VisualAgent(car, occupiedPoint);

			visag.getSprite().draw(visag.getPosition().getX(), visag.getPosition().getY());
		}

	}

	@Override
	public boolean closeRequested() {
		return false;
	}

	

}