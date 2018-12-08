package antonafanasjew.cosmodog;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendarTest;
import antonafanasjew.cosmodog.camera.test.CamTest;
import antonafanasjew.cosmodog.notifications.NotificationQueueTest;
import antonafanasjew.cosmodog.timing.ChronometerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		PlanetaryCalendarTest.class, 
		CamTest.class,
		NotificationQueueTest.class,
		ChronometerTest.class,
	}
)
public class TestSuite {

}
