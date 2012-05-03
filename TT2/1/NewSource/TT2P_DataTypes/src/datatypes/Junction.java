package datatypes;

import com.gigaspaces.annotation.pojo.SpaceClass;

@SpaceClass
public class Junction {
	public static final String EMPTY = "EMPTY";

	public static int nextJuncId = 0;

	private Integer id;
	private String occupiedById = EMPTY;
	
	public Junction() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOccupiedById() {
		return occupiedById;
	}

	public void setOccupiedById(String occupiedById) {
		this.occupiedById = occupiedById;
	}

	public Junction(Integer id, String occupiedById) {
		super();
		this.id = id;
		this.occupiedById = occupiedById;
	}



}
