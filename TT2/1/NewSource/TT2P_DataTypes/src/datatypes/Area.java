package datatypes;

import org.lwjgl.util.Point;

import com.gigaspaces.annotation.pojo.SpaceClass;

@SpaceClass
public class Area {
	public static final String EMPTY = "EMPTY";
	
	public static final Integer NO_JUNCTION = -1;

	private Integer id;
	private Point pos;
	private AreaDirection dir;
	private AreaSide side;
	private boolean spawn;
	private String occupiedById = EMPTY;
	private boolean junction;
	private Integer juncId = NO_JUNCTION;
	
	public Area() {

	}

	public Area(Integer id, Point pos, AreaDirection dir, AreaSide side, boolean spawn, boolean junction) {
		super();
		this.id = id;
		this.pos = pos;
		this.dir = dir;
		this.side = side;
		this.spawn = spawn;
		this.junction = junction;
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

	public boolean isJunction() {
		return junction;
	}

	public void setJunction(boolean junction) {
		this.junction = junction;
	}

	public Integer getJuncId() {
		return juncId;
	}

	public void setJuncId(Integer juncId) {
		this.juncId = juncId;
	}

}
