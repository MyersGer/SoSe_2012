import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import datatypes.Junction;

public class SimpleGame extends BasicGame {

	private String map_file, mapgfx_location;
	private TiledMap map;

	static public enum OridnalDirection {
		UP, LEFT, RIGHT, DOWN, LEFT_UP, RIGHT_UP, LEFT_DOWN, RIGHT_DOWN
	}

	private static Point LEFT_UP = new Point(-1, -1);
	private static Point LEFT = new Point(-1, 0);
	private static Point LEFT_DOWN = new Point(-1, 1);
	private static Point DOWN = new Point(0, 1);
	private static Point RIGHT_DOWN = new Point(1, 1);
	private static Point RIGHT = new Point(1, 0);
	private static Point RIGHT_UP = new Point(1, -1);
	private static Point UP = new Point(0, -1);

	private Point directionToPoint(OridnalDirection direction) {
		switch (direction) {
		case LEFT_UP:
			return LEFT_UP;
		case LEFT:
			return LEFT;
		case LEFT_DOWN:
			return LEFT_DOWN;
		case DOWN:
			return DOWN;
		case RIGHT_DOWN:
			return RIGHT_DOWN;
		case RIGHT:
			return RIGHT;
		case RIGHT_UP:
			return RIGHT_UP;
		case UP:
			return UP;
		default:
			throw new IllegalArgumentException(direction.toString() + " unknown direction can not be mapped to Point");
		}
	}

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

				boolean junction = false;
				String junctionString = map.getTileProperty(tileId, "crossroad", "false");
				if (!junctionString.equals("false")) {
					junction = true;
				}

				areaArr[areaId] = new Area(areaId, new Point(x, y), dir, side, spawn, junction);

				areaId++;

			}
		}
		areaArr = buildJunctions(areaArr);

		gigaSpace.writeMultiple(areaArr);
		spawnCars();

		gc.exit();
	}

	private Area[] buildJunctions(Area[] areaArr) {
		List<Area> arreasWithJuncs = new ArrayList<Area>();
		for (Area area : areaArr) {
			
			if (area.isJunction() && area.getJuncId() == Area.NO_JUNCTION) {
				Junction.nextJuncId++;
				Junction junction = new Junction(Junction.nextJuncId, Junction.EMPTY);
				gigaSpace.write(junction);
				for (Point point : neighbors(area.getPos())) {
					Area neighArea = getAreaFromPoint(point, areaArr);
					if (neighArea.isJunction()) {
						neighArea.setJuncId(junction.getId());
					}
					area.setJuncId(junction.getId());
				}
			}

			arreasWithJuncs.add(area);
		}

		return arreasWithJuncs.toArray(new Area[0]);
	}

	private Area getAreaFromPoint(Point point, Area[] arreas) {
		for (Area area : arreas) {
			if (area.getPos().equals(point))
				return area;
		}
		return null;
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

	// get all neighbors of loc
	private Set<Point> neighbors(Point loc) {
		Set<Point> neighbors = new HashSet<Point>();
		for (OridnalDirection direction : Arrays.asList(OridnalDirection.values())) {
			neighbors.add(destination(loc, direction));
		}
		return neighbors;
	}

	private Point destination(Point loc, OridnalDirection direction) {
		Point directionAsPoint = directionToPoint(direction);
		int xNew = directionAsPoint.getX() + loc.getX();
		int yNew = directionAsPoint.getY() + loc.getY();
		return new Point(xNew, yNew);
	}
}