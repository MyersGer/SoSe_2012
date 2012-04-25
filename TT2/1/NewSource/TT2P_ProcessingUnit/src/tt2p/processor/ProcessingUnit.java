package tt2p.processor;

import java.util.logging.Logger;

import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;
import org.openspaces.events.polling.ReceiveHandler;
import org.openspaces.events.polling.receive.MultiTakeReceiveOperationHandler;
import org.openspaces.events.polling.receive.ReceiveOperationHandler;

import com.j_spaces.core.client.SQLQuery;

import datatypes.Car;

@EventDriven
@Polling(concurrentConsumers = 6)
public class ProcessingUnit {
	Logger logger = Logger.getLogger(this.getClass().getName());

	public ProcessingUnit() {
		logger.info("Processor instantiated, waiting for cars...");
	}

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
		return actCar;
	}

}
