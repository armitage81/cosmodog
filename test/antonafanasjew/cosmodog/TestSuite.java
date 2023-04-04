package antonafanasjew.cosmodog;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendarTest;
import antonafanasjew.cosmodog.camera.test.CamTest;
import antonafanasjew.cosmodog.notifications.NotificationQueueTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		PlanetaryCalendarTest.class, 
		CamTest.class,
		NotificationQueueTest.class
	}
)
public class TestSuite {

}
