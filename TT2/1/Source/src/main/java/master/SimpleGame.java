package master;



public class SimpleGame extends BasicGame {

	private String map_file, mapgfx_location;
	private TiledMap map;
	private World world;
	private GigaSpace gigaSpace;

	public SimpleGame(String map_file, String mapgfx_location) {
		super("TupleSpace");

		this.map_file = map_file;
		this.mapgfx_location = mapgfx_location;
		gigaSpace = DataGridConnectionUtility.getSpace("DrivingSpace", 1, 0);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {

		map = new TiledMap(map_file, mapgfx_location);
		Area[] areaArr = new Area[map.getHeight() * map.getWidth()];

		gc.setTargetFrameRate(50);
		int streetLayerIndex = map.getLayerIndex("strassennetz");

		int areaId = 0;
		// collect tiles where cars can be spawned, create mapping tileID->XY,
		// init tuplespace
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

	private void spawnCars() {
		// tue spawn point mal

		// hohlt freien spawn point aus tuple space
		SQLQuery<Area> spawnQuery = new SQLQuery<Area>(Area.class, "spawn=TRUE and occupiedById = -1");

		Area spawnPoint;
		spawnPoint = gigaSpace.take(spawnQuery);
		while (spawnPoint != null) {
			Car car = new Car(false);

			// setzt auto auf mapfield unter spawn point

			// schriebt auto in tuple space

			// schreibt das nun besetzte mapfield in tuple space

			spawnPoint = gigaSpace.take(spawnQuery);
		}

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

}