import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class SimpleGame extends BasicGame {

	private TiledMap map;
	private Image agentIMG = null;

//	private static Logger logger;

	private static final int DISPLAY_WIDTH = 800;
	private static final int DISPLAY_HEIGHT = 800;

	private static final String TILED_MAP_LOCATION = "maps/map_campus_berliner_tor.tmx";
	private static final String TILED_RESOURCE_LOCATION = "maps";

	private static AppGameContainer app;
	long lastTime = System.nanoTime();// , beforeTime, afterTime, timeDiff,
										// sleepTime;

	public SimpleGame() {
		super("Slick2DPath2Glory - SimpleGame");

		System.out.println(System.getProperty("user.dir"));

	}

	@Override
	public void init(GameContainer gc) throws SlickException {
	//	logger = Logger.getLogger("SimpleGame");

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		Date startDate;
		try {
			startDate = dateFormat.parse("7.12.2011 12:05");
		} catch (ParseException e) {
		//	logger.error("Fehler beim Datumparsen. Aktuelle Zeit wird genommen!");
			startDate = new Date(System.currentTimeMillis());
		}

		map = new TiledMap(TILED_MAP_LOCATION, TILED_RESOURCE_LOCATION);

		agentIMG = new Image("gfx/stickman.png");
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		boolean moved = false;
		//logger.debug("update");
		processInput(gc.getInput());

	

		if (!moved) {

			app.destroy();
		}

		
	}

	private void processInput(Input in) {
		if (in.isKeyDown(Input.KEY_DOWN)) {
		
		} else if (in.isKeyDown(Input.KEY_UP)) {

		} else if (in.isKeyDown(Input.KEY_LEFT)) {

		} else if (in.isKeyDown(Input.KEY_RIGHT)) {

		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		// map.render(0, 0, 0, 0, map.getHeight(), map.getWidth());
		int tileWidth = map.getTileWidth();
		int tileHeight = map.getTileHeight();

		

		// Mittelpunkt bestimmen
		int centerx = (DISPLAY_WIDTH / tileWidth) / 2;
		int centery = (DISPLAY_HEIGHT / tileHeight) / 2;



		map.render(0, 0, 0, 0, map.getHeight(), map.getWidth());

		agentIMG.draw((centerx - 0.5f) * tileWidth, (centery - 1) * tileHeight, 2 * tileWidth, 2 * tileHeight);

		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, 180, 280);
		g.setColor(new Color(255, 255, 255));
		

	}

	public static void main(String[] args) throws SlickException {
		app = new AppGameContainer(new SimpleGame());

		app.setDisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT, false);
		
		app.start();
	}

	@Override
	public boolean closeRequested() {
		// TODO Auto-generated method stub
		return false;
	}

}