package world;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import common.IUpdateable;

import org.lwjgl.util.Point;
import org.newdawn.slick.tiled.TiledMap;
import org.openspaces.core.GigaSpace;

import common.world.OccupationAreaTuple;

import tuplespace.TupleSpace;


import agent.Agent;
import agent.Direction;
import agent.controller.AutonomousController;
import agent.controller.InteractiveController;



public class World implements IUpdateable{
	
	private TiledMap map;
	private ArrayList<Agent> agents = new ArrayList<Agent>();
	private ArrayList<Integer> spawnAreas = new ArrayList<Integer>();
	private HashMap<Integer, Point> areaIDToPointMap = new HashMap<Integer, Point>();
	private HashMap<Point, Integer> areaPointToIDMap = new HashMap<Point, Integer>();
	private int streetLayerIndex = 0;
	
	public World(TiledMap map, int agentCount){
		this.map = map;
		streetLayerIndex = map.getLayerIndex("strassennetz");
		init();
	}
	
	public void init(){
		//TODO: mapinformationen aus tuplespace lesen (areaidtopointmap, ...)
	}
		
	public void update(){
		agents.clear();
		//TODO: agentlist aus tuplespace lesen
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
		agentLowerRight = new Point(agentUpperLeft.getX()+a.getWidth(), agentUpperLeft.getY()+a.getHeight());
		
		Point area1 = areaForPoint(agentUpperLeft);
		Point area2 = areaForPoint(agentLowerRight);
		areas.add(area1);
		if(!area1.equals(area2))
			areas.add(area2);
		
		return areas;
	}
	
	//TODO: spawn in agent verschieben und agentlist im tuplespace halten
	private void spawn(){
		if(!this.nonActiveAgents.isEmpty()){
			Random rand = new Random(System.nanoTime());
			int pos = rand.nextInt(this.spawnAreas.size());
			
			OccupationAreaTuple tt = gigaSpace.readById(OccupationAreaTuple.class, spawnAreas.get(pos));

			if(tt != null){
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
	
	public ArrayList<Agent> getAgentList(){
		return this.agents;
	}
	
}
