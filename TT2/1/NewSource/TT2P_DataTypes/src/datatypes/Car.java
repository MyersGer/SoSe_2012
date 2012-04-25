package datatypes;

import java.util.UUID;

import com.gigaspaces.annotation.pojo.SpaceClass;

@SpaceClass
public class Car {

	//TODO add reference, car should have reference to map tile it occupies. maybe remove ref to car from maptile 
	private Direction direction;
	private boolean isInteractive;
	private boolean isActive = false;
	private String id;
	private Integer occupiedArea;
	
	public Car() {

	}

	public Car(boolean isInteractive, Direction direction, Integer occupiedArea) {
		this.isInteractive = isInteractive;
		this.direction = direction;
		this.occupiedArea = occupiedArea;
		this.id = UUID.randomUUID().toString();
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean isInteractive() {
		return isInteractive;
	}

	public void setInteractive(boolean isInteractive) {
		this.isInteractive = isInteractive;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOccupiedArea() {
		return occupiedArea;
	}

	public void setOccupiedArea(Integer occupiedArea) {
		this.occupiedArea = occupiedArea;
	}
	

}
