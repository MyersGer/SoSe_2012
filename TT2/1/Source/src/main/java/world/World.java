package world;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.util.Point;
import org.newdawn.slick.tiled.TiledMap;

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
	private HashMap<Integer, Point> streetTileXYMap = new HashMap<Integer, Point>();
	private Agent player;
	private int updates = 499;
	
	public World(TiledMap map, int agentCount){
		this.map = map;
		init(agentCount);
	}
	
	private void init(int agentCount){
		
		int streetLayerIndex  = map.getLayerIndex("strassennetz");
	
		//collect tiles where cars can be spawned and create mapping tileID->XY
		for(int x=0; x<map.getWidth(); x++){
			for(int y=0; y<map.getHeight(); y++){
				int tileID = map.getTileId(x, y, streetLayerIndex);
				streetTileXYMap.put(tileID, new Point(x, y)); //create mapping
				
				//check for spawn property
				String prop = map.getTileProperty(tileID, "spawn", "false");
				if(prop.equals("true"))
					spawnTiles.add(tileID);
				
			}
		}
		
		//initialize Agents
		for(int i=0; i<agentCount; i++){
			
			Agent agent = null;
			
			//first Agent is always Player
			if(i==0){
				agent = new Agent("src/main/resources/objects/car_pc.png");
				agent.setController(new InteractiveController(this, agent));
				this.player = agent;
			}else{
				agent = new Agent("src/main/resources/objects/car_npc.png");
				agent.setController(new AutonomousController(this, agent));
			}
			
			this.nonActiveAgents.add(agent);
		}
		
		//TODO: tiles in tuplespace werfen
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
	
	
	
	public ArrayList<Point> getOccupiedTiles(Agent a){
		ArrayList<Point> tiles = new ArrayList<Point>();
		
		Point agentAbsPos = a.getPosition();

		
		return tiles;
	}
	
	private void spawn(){
		if(!this.nonActiveAgents.isEmpty()){
			Random rand = new Random(System.nanoTime());
			int pos = rand.nextInt(this.spawnTiles.size());
			//TODO:gegen tupelspace prüfen

			Agent agent = this.nonActiveAgents.poll();
			Point tilePos = this.streetTileXYMap.get(this.spawnTiles.get(pos));
			agent.setPosition(tilePos, new Point(tilePos.getX()*map.getTileWidth(), tilePos.getY()*map.getTileHeight())); //set agent's tile to spawn pos
			
			String direction = map.getTileProperty(this.spawnTiles.get(pos), "direction", "false");
			String side = map.getTileProperty(this.spawnTiles.get(pos), "side", "false");
			
			if(direction.equals("horizontal")){
				if(side.equals("right")){
					agent.setDirection(Direction.LEFT_TO_RIGHT);
				}else{
					agent.setDirection(Direction.RIGHT_TO_LEFT);
				}
			}else{ //vertical
				if(side.equals("right")){
					agent.setDirection(Direction.TOP_TO_BOTTOM);
				}else{
					agent.setDirection(Direction.BOTTOM_TO_TOP);
				}
			}
			this.activeAgents.add(agent);
		}
	}
	
	public Agent getPlayer(){
		return this.player;
	}
	
	
	public ArrayList<Agent> getAgentList(){
		return this.activeAgents;
	}
	
}
