package datatypes;

import org.lwjgl.util.Point;

public class Dimension {
	Integer xMax;
	Integer yMax;

	public Integer getxMax() {
		return xMax;
	}

	public void setxMax(Integer xMax) {
		this.xMax = xMax;
	}

	public Integer getyMax() {
		return yMax;
	}

	public void setyMax(Integer yMax) {
		this.yMax = yMax;
	}

	public Dimension(Integer xMax, Integer yMax) {
		super();
		this.xMax = xMax;
		this.yMax = yMax;
	}

	public Dimension() {
	}
	

}
