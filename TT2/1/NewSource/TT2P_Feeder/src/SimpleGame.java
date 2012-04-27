
import org.lwjgl.util.Point;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.openspaces.core.GigaSpace;

import com.j_spaces.core.client.SQLQuery;

import datatypes.Area;
import datatypes.AreaDirection;
import datatypes.AreaSide;
import datatypes.Car;
import datatypes.Dimension;
import datatypes.Direction;

public class SimpleGame extends BasicGame {

	private String map_file, mapgfx_location;
	private TiledMap map;

	private GigaSpace gigaSpace;

	public SimpleGame(String map_file, String mapgfx_location) {
		super("TupleSpace");

		this.map_file = map_file;
		this.mapgfx_location = mapgfx_location;
		gigaSpace = DataGridConnectionUtility.getSpace("space", 1, 0);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {

		map = new TiledMap(map_file, mapgfx_location);
		Area[] areaArr = new Area[map.getHeight() * map.getWidth()];

		gc.setTargetFrameRate(50);
		int streetLayerIndex = map.getLayerIndex("strassennetz");

		int areaId = 0;
		// collect tiles where cars can be spawned, create mapping tileID->XY,
		
		gigaSpace.write(new Dimension(map.getWidth(), map.getHeight()));
		
		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				int tileId = map.getTileId(x, y, streetLayerIndex);

				// check for spawn property
				String propSpawn = map.getTileProperty(tileId, "spawn", "false");
				boolean spawn = false;
				if (propSpawn.equals("true")) {
					spawn = true;
				}

				AreaDirection dir = null;
				String propDirection = map.getTileProperty(tileId, "direction", "horizontal");
				if (propDirection.equals("horizontal"))
					dir = AreaDirection.HORIZONTAL;
				else if (propDirection.equals("vertical"))
					dir = AreaDirection.VERTICAL;

				AreaSide side = null;
				String propSide = map.getTileProperty(tileId, "side", "left");
				if (propSide.equals("left"))
					side = AreaSide.LEFT;
				else if (propSide.equals("right"))
					side = AreaSide.RIGHT;

				areaArr[areaId] = new Area(areaId, new Point(x, y), dir, side, spawn);

				areaId++;
				
			}
		}
		gigaSpace.writeMultiple(areaArr);
		spawnCars();
		
		gc.exit();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

	}

	public void render(GameContainer gc, Graphics g) throws SlickException {

	}

	@Override
	public boolean closeRequested() {
		return false;
	}

	private void spawnCars() {
		// tue spawn point mal

		// hohlt freien spawn point aus tuple space
		SQLQuery<Area> spawnQuery = new SQLQuery<Area>(Area.class, "spawn=TRUE and occupiedById = '" + Area.EMPTY + "'");

		Area spawnPoint;
		spawnPoint = gigaSpace.take(spawnQuery);
		while (spawnPoint != null) {
			Car car = new Car(false, getInitialCarDirection(spawnPoint), spawnPoint.getId(), spawnPoint.getPos());
			
			spawnPoint.setOccupiedById(car.getId());

			gigaSpace.write(car);
			gigaSpace.write(spawnPoint);

			// take next spawnPoint
			spawnPoint = gigaSpace.take(spawnQuery);
		}
	}

	private Direction getInitialCarDirection(Area area) {

		Direction dir = null;
		if (area.getDir().equals(AreaDirection.HORIZONTAL)) {
			if (area.getSide().equals(AreaSide.RIGHT)) {
				dir = Direction.LEFT_TO_RIGHT;
			} else {
				dir = Direction.RIGHT_TO_LEFT;
			}
		} else { // vertical
			if (area.getSide().equals(AreaSide.RIGHT)) {
				dir = Direction.TOP_TO_BOTTOM;
			} else {
				dir = Direction.BOTTOM_TO_TOP;
			}
		}
		return dir;
	}
}