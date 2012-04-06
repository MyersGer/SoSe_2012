/*package basic;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.openspaces.admin.gsm.GridServiceManager;
import org.openspaces.admin.pu.ProcessingUnit;
import org.openspaces.admin.pu.ProcessingUnitAlreadyDeployedException;
import org.openspaces.admin.space.SpaceDeployment;
import org.openspaces.core.GigaSpace;

import com.j_spaces.core.client.SQLQuery;

public class TupleMain {

	protected final static String SPACE_NAME = "foobar1111111";

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("TupleSpace");
		TupleSpace tp = new TupleSpace(SPACE_NAME);
		GigaSpace gigaSpace = tp.getGigaSpace();

		gigaSpace.write(new Person(1, "Vincent", "Chase"));
		gigaSpace.write(new Person(2, "Johny", "Drama"));

		// read by ID
		Person vince = gigaSpace.readById(Person.class, 1);

		// read with SQL query
		Person johny = gigaSpace.read(new SQLQuery(Person.class, "firstName=?", "Johny"));
		logger.debug("Johny ");
		// readMultiple with template
		Person[] vinceAndJohny = gigaSpace.readMultiple(new Person());

	}
}
*/