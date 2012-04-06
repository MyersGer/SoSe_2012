/*package basic;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.openspaces.admin.gsm.GridServiceManager;
import org.openspaces.admin.pu.ProcessingUnit;
import org.openspaces.admin.pu.ProcessingUnitAlreadyDeployedException;
import org.openspaces.admin.pu.ProcessingUnits;
import org.openspaces.admin.space.SpaceDeployment;
import org.openspaces.core.GigaSpace;

public class TupleSpace {
	GigaSpace gigaSpace;
	Logger logger = Logger.getLogger("TupleSpace");

	public TupleSpace(String spaceName) {
		logger.debug("creating TupleSpace");
		Admin admin = new AdminFactory().createAdmin();
		try {
			ProcessingUnit pu;
			// create an admin instance to interact with the cluster
			

			// locate a grid service manager and deploy a partioned data grid
			// with 1 primaries and one backup for each primary
			GridServiceManager esm = admin.getGridServiceManagers().waitForAtLeastOne();
			logger.info("run startup1.sh, if nothing happens");
			pu = esm.deploy(new SpaceDeployment(spaceName).partitioned(1, 0));
			admin.getProcessingUnits();
			
			// Once your data grid has been deployed, wait for 4 instances (2
			// primaries and 2 backups)
			pu.waitFor(0, 5, TimeUnit.SECONDS);
			logger.info("run startup2.sh, if nothing happens");
			
			// and finally, obtain a reference to it
			gigaSpace = pu.waitForSpace().getGigaSpace();

		
		} catch (ProcessingUnitAlreadyDeployedException e) {
			logger.fatal("Space exists", e);
			ProcessingUnits pus = admin.getProcessingUnits();
			ProcessingUnit pu = pus.getProcessingUnit(spaceName);
			gigaSpace = pu.waitForSpace().getGigaSpace();
		}
	}
	
	public GigaSpace getGigaSpace() {
		return gigaSpace;
	}

}*/
