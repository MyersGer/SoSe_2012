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
		return receiveHandler;
	}

	@SpaceDataEvent
	public Car execute(Car actCar) {
		System.out.println("Car: " + actCar.getId());

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Date now = new Date();
		Long longTime = new Long(now.getTime() / 1000);

		// get new position
		SQLQuery<Area> oldAreaQuery = new SQLQuery<Area>(Area.class, "occupiedById = '" + actCar.getId() + "'");
		Area oldArea = gigaspace.take(oldAreaQuery);
		Direction direction = actCar.getDirection();
		Point nextPoint = getNextAreaCordsInDirection(oldArea, direction);

		// try to take empty place for that.
		SQLQuery<Area> nextAreaQuery = new SQLQuery<Area>(Area.class, "pos.x = " + nextPoint.getX() + " and pos.y = " + nextPoint.getY() + " and occupiedById = '" + Area.EMPTY + "'");
		Area newArea = gigaspace.take(nextAreaQuery);

		System.out.println("Car: " + actCar.getId() + " new Position: " + nextPoint.getX() + " " + nextPoint.getY());
		if (newArea != null) {
			Movement movement = new Movement(newArea.getPos(), actCar.getId(), longTime.intValue(), actCar.getDirection(), actCar.isInteractive());
			gigaspace.write(movement);
			
			actCar.setOccupiedArea(newArea.getId());
			newArea.setOccupiedById(actCar.getId());
			oldArea.setOccupiedById(Area.EMPTY);
			gigaspace.write(newArea);
			gigaspace.write(oldArea);
			
		}

		return actCar;
	}

	// TODO wrap on end of coordinate space
	protected Point getNextAreaCordsInDirection(Area from, Direction direction) {
		Point to = null;

		Point pointFrom = from.getPos();
		switch (direction) {
		case BOTTOM_TO_TOP:
			to = new Point(pointFrom.getX(), pointFrom.getY() - 1);
			break;
		case TOP_TO_BOTTOM:
			to = new Point(pointFrom.getX(), pointFrom.getY() + 1);
			break;
		case LEFT_TO_RIGHT:
			to = new Point(pointFrom.getX() + 1, pointFrom.getY());
			break;
		case RIGHT_TO_LEFT:
			to = new Point(pointFrom.getX() - 1, pointFrom.getY());
			break;
		default:
			logger.warning("Car trying to move in unknown Direction, not doing anything...");
			break;
		}

		return to;
	}
	
	public int norm(int kor) {
		SQLQuery<Dimension> dimensionQuery = new SQLQuery<Dimension>(Dimension.class, "");
		Dimension dimension = gigaspace.read(dimensionQuery);
		kor = kor % dimension.getxMax();
		if(kor < 0)
			kor = kor + dimension.getxMax();
		return kor;
	}

}
