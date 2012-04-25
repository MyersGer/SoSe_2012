package datatypes;

import java.util.UUID;

import com.gigaspaces.annotation.pojo.SpaceClass;

@SpaceClass
public class Car {

	private Direction direction = Direction.LEFT_TO_RIGHT;
	private boolean isInteractive;
	private boolean isActive = false;
	private String id;

	public Car() {

	}

	public Car(boolean isInteractive) {
		this.isInteractive = isInteractive;
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

}
