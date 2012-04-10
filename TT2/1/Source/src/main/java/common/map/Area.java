package common.map;

import org.lwjgl.util.Point;

import com.gigaspaces.lrmi.ProtocolAdapter.Side;

import agent.Direction;

public class Area {
	private Integer id;
	private Point pos;
	private AreaDirection dir;
	private AreaSide side;
	private boolean spawn;
	
	public Area(){
		
	}

	public Area(Integer id, Point pos, AreaDirection dir, AreaSide side, boolean spawn) {
		super();
		this.id = id;
		this.pos = pos;
		this.dir = dir;
		this.side = side;
		this.spawn = spawn;
	}

	
}
