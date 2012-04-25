package datatypes;


import org.lwjgl.util.Point;

import com.gigaspaces.annotation.pojo.SpaceClass;

@SpaceClass
public class Area {
	public static final String EMPTY = "EMPTY";
	
	private Integer id;
	private Point pos;
	private AreaDirection dir;
	private AreaSide side;
	private boolean spawn;
	private String occupiedById = EMPTY;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Point getPos() {
		return pos;
	}

	public void setPos(Point pos) {
		this.pos = pos;
	}

	public AreaDirection getDir() {
		return dir;
	}

	public void setDir(AreaDirection dir) {
		this.dir = dir;
	}

	public AreaSide getSide() {
		return side;
	}

	public void setSide(AreaSide side) {
		this.side = side;
	}

	public boolean isSpawn() {
		return spawn;
	}

	public void setSpawn(boolean spawn) {
		this.spawn = spawn;
	}

	public String getOccupiedById() {
		return occupiedById;
	}

	public void setOccupiedById(String occupiedById) {
		this.occupiedById = occupiedById;
	}

	
}
