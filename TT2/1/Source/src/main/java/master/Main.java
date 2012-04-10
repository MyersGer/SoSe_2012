package master;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {

	private final static String TILED_MAP_LOCATION = "src/main/resources/maps/strassen.tmx";
	private final static String TILED_RESOURCE_LOCATION = "src/main/resources/maps/";
	
	private static final int DISPLAY_WIDTH = 1056;
	private static final int DISPLAY_HEIGHT = 816;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws SlickException {
		
		System.out.println(System.getProperty("user.dir"));
		AppGameContainer app;
		app = new AppGameContainer(new SimpleGame(TILED_MAP_LOCATION, TILED_RESOURCE_LOCATION));

		app.setDisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT, false);
		
		app.start();
	}

}
