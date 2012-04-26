package datatypes;

import org.lwjgl.util.Point;

public class Movement {
	private Point position;
	private String carId;
	private Integer time;
	private Direction direction;
	private Boolean interactive;

	public Movement() {
	}

	public Movement(Point location, String carId, Integer time, Direction direction, Boolean interactive) {
		super();
		this.position = location;
		this.carId = carId;
		this.time = time;
		this.direction = direction;
		this.interactive = interactive;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Boolean getInteractive() {
		return interactive;
	}

	public void setInteractive(Boolean interactive) {
		this.interactive = interactive;
	}

	public Point getLocation() {
		return position;
	}

	public void setLocation(Point location) {
		this.position = location;
	}

}
