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
	private ArrayList<Integer> spawnAreas = new ArrayList<Integer>();
	private HashMap<Integer, Point> areaIDToPointMap = new HashMap<Integer, Point>();
	private HashMap<Point, Integer> areaPointToIDMap = new HashMap<Point, Integer>();
	private Agent player;
	private int updates = 499;
	private GigaSpace gigaSpace = null;
	private int streetLayerIndex = 0;
	
	public World(TiledMap map, int agentCount){
		this.map = map;
		streetLayerIndex = map.getLayerIndex("strassennetz");
		init(agentCount);
	}
	
	private void init(int agentCount){
		 	
		Integer areaId = 0; 
		
		TupleSpace tp = new TupleSpace("TileSpace");
		gigaSpace = tp.getGigaSpace();
	
		//collect tiles where cars can be spawned, create mapping tileID->XY, init tuplespace 
	
		for(int x=0; x<map.getWidth(); x++){
			for(int y=0; y<map.getHeight(); y++){
				int tileId = map.getTileId(x, y, streetLayerIndex);
				areaIDToPointMap.put(areaId, new Point(x, y)); //create mapping
				areaPointToIDMap.put(new Point(x,y), areaId);
				
				//check for spawn property
				String prop = map.getTileProperty(tileId, "spawn", "false");
				if(prop.equals("true"))
					spawnAreas.add(areaId);
				
				
				//init tuplespace
				gigaSpace.write(new AreaTuple(areaId));
				
				areaId++;
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
	
	private Point areaForPoint(Point p){
		int x = (int) Math.floor(p.getX()/map.getTileWidth());
		int y = (int) Math.floor(p.getY()/map.getTileHeight());
		return new Point(x, y);
	}
	
	public Integer getAreaIdForAreaCoord(Point area){
		return this.areaPointToIDMap.get(area);
	}
	
	public ArrayList<Point> getOccupiedAreas(Agent a){
		ArrayList<Point> areas = new ArrayList<Point>();
				
		Point agentUpperLeft;
		Point agentLowerRight;
		
		agentUpperLeft = a.getPosition();
		agentLowerRight = new Point(agentUpperLeft.getX()+a.getSprite().getWidth(), agentUpperLeft.getY()+a.getSprite().getHeight());
		
		Point area1 = areaForPoint(agentUpperLeft);
		Point area2 = areaForPoint(agentLowerRight);
		areas.add(area1);
		if(!area1.equals(area2))
			areas.add(area2);
		
		return areas;
	}
	
	private void spawn(){
		if(!this.nonActiveAgents.isEmpty()){
			Random rand = new Random(System.nanoTime());
			int pos = rand.nextInt(this.spawnAreas.size());
			
			AreaTuple tt = gigaSpace.readById(AreaTuple.class, spawnAreas.get(pos));

			if(tt != null){
				Agent agent = this.nonActiveAgents.poll();
				Point area = this.areaIDToPointMap.get(this.spawnAreas.get(pos));
				
				int tileId = map.getTileId(area.getX(), area.getY(), streetLayerIndex);
				String direction = map.getTileProperty(tileId, "direction", "false");
				String side = map.getTileProperty(tileId, "side", "false");
				
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
				Point absPos = new Point(area.getX()*map.getTileWidth(), area.getY()*map.getTileHeight());
				agent.spawn(area,  absPos, dir);
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
