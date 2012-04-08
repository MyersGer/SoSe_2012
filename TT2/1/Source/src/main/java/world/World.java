package world;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.util.Point;
import org.newdawn.slick.tiled.TiledMap;
import org.openspaces.core.GigaSpace;

import tuplespace.TupleSpace;

import basic.IUpdateable;

import agent.Agent;
import agent.Direction;
import agent.controller.AutonomousController;
import agent.controller.InteractiveController;



public class World implements IUpdateable{
	
	private TiledMap map;
	private ArrayList<Agent> activeAgents = new ArrayList<Agent>();
	private Queue<Agent> nonActiveAgents = new LinkedBlockingQueue<Agent>();
	private ArrayList<Integer> spawnTiles = new ArrayList<Integer>();
	private HashMap<Integer, Point> tileIDtoPointMap = new HashMap<Integer, Point>();
	private HashMap<Point, Integer> tilePointToIDMap = new HashMap<Point, Integer>();
	private Agent player;
	private int updates = 499;
	private GigaSpace gigaSpace = null;
	
	public World(TiledMap map, int agentCount){
		this.map = map;
		init(agentCount);
	}
	
	private void init(int agentCount){
		 	
		TupleSpace tp = new TupleSpace("TileSpace");
		gigaSpace = tp.getGigaSpace();
		
		int streetLayerIndex  = map.getLayerIndex("strassennetz");
	
		//collect tiles where cars can be spawned, create mapping tileID->XY, init tuplespace 
	
		for(int x=0; x<map.getWidth(); x++){
			for(int y=0; y<map.getHeight(); y++){
				int tileID = map.getTileId(x, y, streetLayerIndex);
				tileIDtoPointMap.put(tileID, new Point(x, y)); //create mapping
				tilePointToIDMap.put(new Point(x,y), tileID);
				
				//check for spawn property
				String prop = map.getTileProperty(tileID, "spawn", "false");
				if(prop.equals("true"))
					spawnTiles.add(tileID);
				
				
				//init tuplespace
				gigaSpace.write(new TileTuple(tileID));
			}
		}
		
		//initialize Agents
		for(int i=0; i<agentCount; i++){
			
			Agent agent = null;
			
			//first Agent is always Player
			if(i==0){
				agent = new Agent("src/main/resources/objects/car_pc.png");
				agent.setController(new InteractiveController(this, agent, gigaSpace));
				this.player = agent;
			}else{
				agent = new Agent("src/main/resources/objects/car_npc.png");
				agent.setController(new AutonomousController(this, agent, gigaSpace));
			}
			
			this.nonActiveAgents.add(agent);
		}
	}
	
	public void update(){
		updates++;
		if(this.updates > 200){
			spawn();
			this.updates = 0;
		}
		
		ArrayList<Integer> removeActiveAgents = new ArrayList<Integer>();
		for(Agent a: activeAgents){
			a.update();
			
			//remove agent from active list when leaving map
			Point agentAbsPos = a.getPosition();
			if(agentAbsPos.getX() > map.getWidth()*map.getTileWidth() || agentAbsPos.getX() < 0){
				nonActiveAgents.add(a);
				removeActiveAgents.add(activeAgents.indexOf(a));
			}
			
			if(agentAbsPos.getY() > map.getHeight()*map.getTileHeight() || agentAbsPos.getY() < 0){
				nonActiveAgents.add(a);
				removeActiveAgents.add(activeAgents.indexOf(a));
			}
		}

		for(int x: removeActiveAgents){
			activeAgents.remove(x);
		}
	}
	
	private Point tileForPoint(Point p){
		int x = (int) Math.floor(p.getX()/map.getTileWidth());
		int y = (int) Math.floor(p.getY()/map.getTileHeight());
		return new Point(x, y);
	}
	
	public Integer getTileIdForTileCoord(Point tile){
		return this.tilePointToIDMap.get(tile);
	}
	
	public ArrayList<Point> getOccupiedTiles(Agent a){
		ArrayList<Point> tiles = new ArrayList<Point>();
		
		Point agentAbsPos = a.getPosition();
		
		Point agentUpperLeft;
		Point agentLowerRight;
		
		agentUpperLeft = a.getPosition();
		agentLowerRight = new Point(agentUpperLeft.getX()+a.getSprite().getWidth(), agentUpperLeft.getY()+a.getSprite().getHeight());
		
		tiles.add(tileForPoint(agentUpperLeft));
		tiles.add(tileForPoint(agentLowerRight));
		
		return tiles;
	}
	
	private void spawn(){
		if(!this.nonActiveAgents.isEmpty()){
			Random rand = new Random(System.nanoTime());
			int pos = rand.nextInt(this.spawnTiles.size());
			
			TileTuple tt = gigaSpace.readById(TileTuple.class, spawnTiles.get(pos));

			if(tt != null){
				Agent agent = this.nonActiveAgents.poll();
				Point tilePos = this.tileIDtoPointMap.get(this.spawnTiles.get(pos));
				
				
				String direction = map.getTileProperty(this.spawnTiles.get(pos), "direction", "false");
				String side = map.getTileProperty(this.spawnTiles.get(pos), "side", "false");
				
				Direction dir = null;
				if(direction.equals("horizontal")){
					if(side.equals("right")){
						dir = Direction.LEFT_TO_RIGHT;
					}else{
						dir = Direction.RIGHT_TO_LEFT;
					}
				}else{ //vertical
					if(side.equals("right")){
						dir = Direction.TOP_TO_BOTTOM;
					}else{
						dir = Direction.BOTTOM_TO_TOP;
					}
				}
				Point absPos = new Point(tilePos.getX()*map.getTileWidth(), tilePos.getY()*map.getTileHeight());
				agent.spawn(tilePos,  absPos, dir);
				this.activeAgents.add(agent);
			}
		}
	}
	
	public Agent getPlayer(){
		return this.player;
	}
	
	
	public ArrayList<Agent> getAgentList(){
		return this.activeAgents;
	}
	
}
