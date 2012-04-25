package datatypes;



import java.util.Random;
import java.util.UUID;

import org.lwjgl.util.Point;

import com.gigaspaces.annotation.pojo.SpaceClass;




@SpaceClass
public class Car{
	
	

	private Direction direction = Direction.LEFT_TO_RIGHT;
	private double velocity = 0;
	private double progress = 0;
	private int width, height;
	private boolean isInteractive;
	private boolean isActive = false;
	private String id;
	
	public Car(){
		
	}
	
	public Car(boolean isInteractive){
		width = height = 33;
		Random rnd = new Random(System.nanoTime());
		velocity = rnd.nextDouble();
		if(velocity < 0.1)
			velocity = 0.1;
		if(velocity > 0.5)
			velocity = 1;
		
		this.isInteractive = isInteractive;
		this.id =  UUID.randomUUID().toString();
	}


	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
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
