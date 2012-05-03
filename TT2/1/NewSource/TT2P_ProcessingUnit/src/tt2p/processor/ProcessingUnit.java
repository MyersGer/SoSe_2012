package tt2p.processor;

import java.util.Date;
import java.util.logging.Logger;

import org.lwjgl.util.Point;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;
import org.openspaces.events.polling.ReceiveHandler;
import org.openspaces.events.polling.receive.MultiTakeReceiveOperationHandler;
import org.openspaces.events.polling.receive.ReceiveOperationHandler;

import com.j_spaces.core.client.SQLQuery;

import datatypes.Area;
import datatypes.Car;
import datatypes.Dimension;
import datatypes.Direction;
import datatypes.Junction;
import datatypes.Movement;

@EventDriven
@Polling(concurrentConsumers = 6)
public class ProcessingUnit {
	Logger logger = Logger.getLogger(this.getClass().getName());

	public ProcessingUnit() {
		logger.info("Processor instantiated, waiting for cars...");
	}

	@GigaSpaceContext
	private GigaSpace gigaspace;

	@EventTemplate
	SQLQuery<Car> unprocessedData() {
		SQLQuery<Car> template = new SQLQuery<Car>(Car.class, "");
		return template;
	}

	@ReceiveHandler
	ReceiveOperationHandler receiveHandler() {
		MultiTakeReceiveOperationHandler receiveHandler = new MultiTakeReceiveOperationHandler();
		receiveHandler.setMaxEntries(50);
		// SingleReadReceiveOperationHandler receiveHandler = new
		// SingleReadReceiveOperationHandler();
		return receiveHandler;
	}

	@SpaceDataEvent
	public Car execute(Car actCar) {
		System.out.println("Car: " + actCar.getId());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		boolean areaAvaiable = false;
		boolean junctionAvailable = false;

		// get new position
		SQLQuery<Area> oldAreaQuery = new SQLQuery<Area>(Area.class, "id = '" + actCar.getOccupiedArea() + "'");
		Area oldArea = gigaspace.take(oldAreaQuery);
		if (oldArea != null) {

			Area newArea = getNewArea(actCar, oldArea);
			// can movement be made ?
			areaAvaiable = nextAreaAvailable(newArea);
			junctionAvailable = junctionAvailable(newArea, actCar);

			if (areaAvaiable && junctionAvailable) {
				moveCar(actCar, newArea);
				emptyOldFields(oldArea, newArea);
			}
			if (newArea != null) {
				gigaspace.write(newArea);
			}

			gigaspace.write(oldArea);
		} else {
			System.out.println("Could not take old area");
		}
		return actCar;
	}

	private void emptyOldFields(Area oldArea, Area newArea) {

		oldArea.setOccupiedById(Area.EMPTY);
		if (oldArea.isJunction() && oldArea.getJuncId() != newArea.getJuncId()) {
			SQLQuery<Junction> junctionQuery = new SQLQuery<Junction>(Junction.class, "id = " + oldArea.getJuncId());
			Junction junction = gigaspace.take(junctionQuery);
			junction.setOccupiedById(Junction.EMPTY);
			gigaspace.write(junction);
		}
	}

	private void moveCar(Car actCar, Area newArea) {
		Date now = new Date();
		Long longTime = new Long(now.getTime() / 1000);
		Movement movement = new Movement(newArea.getPos(), actCar.getId(), longTime.intValue(), actCar.getDirection(), actCar.isInteractive());
		actCar.setOccupiedArea(newArea.getId());
		actCar.setPosition(newArea.getPos());
		newArea.setOccupiedById(actCar.getId());
		gigaspace.write(movement);
	}

	private boolean junctionAvailable(Area newArea, Car actCar) {
		boolean junctionAvaiable = false;
		if (newArea == null)
			return junctionAvaiable;

		if (newArea.isJunction()) {
			SQLQuery<Junction> junctionQuery = new SQLQuery<Junction>(Junction.class, "id = " + newArea.getJuncId() + " and ( occupiedById = '" + Junction.EMPTY + "' or occupiedById = '"
					+ actCar.getId() + "')");
			Junction junction = gigaspace.take(junctionQuery);
			if (junction != null) {
				junction.setOccupiedById(actCar.getId());
				gigaspace.write(junction);
				junctionAvaiable = true;
			}

		} else {
			junctionAvaiable = true;
		}
		return junctionAvaiable;

	}

	private boolean nextAreaAvailable(Area newArea) {
		if (newArea != null) {
			return true;
		} else {
			return false;
		}
	}

	private Area getNewArea(Car actCar, Area oldArea) {
		Direction direction = actCar.getDirection();
		Point nextPoint = getNextAreaCordsInDirection(oldArea, direction);

		// try to take empty place for that.
		SQLQuery<Area> nextAreaQuery = new SQLQuery<Area>(Area.class, "pos.x = " + nextPoint.getX() + " and pos.y = " + nextPoint.getY() + " and ( occupiedById = '" + Area.EMPTY
				+ "' or occupiedById = '" + actCar.getId() + "')");
		Area newArea = gigaspace.take(nextAreaQuery);
		return newArea;
	}

	protected Point getNextAreaCordsInDirection(Area from, Direction direction) {
		Point to = null;

		Point pointFrom = from.getPos();
		switch (direction) {
		case BOTTOM_TO_TOP:
			to = new Point(normX(pointFrom.getX()), normY(pointFrom.getY() - 1));
			break;
		case TOP_TO_BOTTOM:
			to = new Point(normX(pointFrom.getX()), normY(pointFrom.getY() + 1));
			break;
		case LEFT_TO_RIGHT:
			to = new Point(normX(pointFrom.getX() + 1), normY(pointFrom.getY()));
			break;
		case RIGHT_TO_LEFT:
			to = new Point(normX(pointFrom.getX() - 1), normY(pointFrom.getY()));
			break;
		default:
			logger.warning("Car trying to move in unknown Direction, not doing anything...");
			break;
		}

		return to;
	}

	public int normX(int kor) {
		SQLQuery<Dimension> dimensionQuery = new SQLQuery<Dimension>(Dimension.class, "");
		Dimension dimension = gigaspace.read(dimensionQuery);
		kor = kor % dimension.getxMax();
		if (kor < 0)
			kor = kor + dimension.getxMax();
		return kor;
	}

	public int normY(int kor) {
		SQLQuery<Dimension> dimensionQuery = new SQLQuery<Dimension>(Dimension.class, "");
		Dimension dimension = gigaspace.read(dimensionQuery);
		kor = kor % dimension.getyMax();
		if (kor < 0)
			kor = kor + dimension.getyMax();
		return kor;
	}

}
