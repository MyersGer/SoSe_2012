import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.openspaces.core.GigaSpace;

import com.j_spaces.core.client.SQLQuery;

import datatypes.Car;
import datatypes.Movement;

public class GUI extends BasicGame {
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	private String map_file, mapgfx_location;
	private TiledMap map;
	private GigaSpace gigaSpace;
	private Map<String, Movement> movementHistory = new HashMap<String, Movement>();

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

		renderMovements();

	}

	private void renderMovements() {
		// SQLQuery<Movement> movementsQuery = new
		// SQLQuery<Movement>(Movement.class, "time > 1 order by time");
		SQLQuery<Movement> movementsQuery = new SQLQuery<Movement>(Movement.class, "");
		Movement[] movements;
		movements = gigaSpace.takeMultiple(movementsQuery, Integer.MAX_VALUE );
		
		buildMovementHistory(movements);
		renderMovementHistory();

	}

	private void renderMovementHistory() {
		for (Movement movement : movementHistory.values()) {
			VisualAgent visag = new VisualAgent(movement.getInteractive(), movement.getDirection(), movement.getLocation());

			visag.getSprite().draw(visag.getPosition().getX(), visag.getPosition().getY());
		}

	}

	private void buildMovementHistory(Movement[] movements) {
		for (Movement movement : movements) {
			Movement carMovementFromHistory = movementHistory.get(movement.getCarId());
			// insert first movement, update others for a car
			if (!movementHistory.containsKey(movement.getCarId())) {
				movementHistory.put(movement.getCarId(), movement);
			} else if (carMovementFromHistory.getTime() < movement.getTime()) {
				movementHistory.put(movement.getCarId(), movement);
			}
		}
	}

	@Override
	public boolean closeRequested() {
		return false;
	}

}