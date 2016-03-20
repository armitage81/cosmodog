package antonafanasjew.cosmodog;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendarTest;
import antonafanasjew.cosmodog.camera.test.CamTest;
import antonafanasjew.cosmodog.notifications.NotificationQueueTest;
import antonafanasjew.cosmodog.timing.ChronometerTest;
import antonafanasjew.cosmodog.writing.model.TextBlockBoxTest;
import antonafanasjew.cosmodog.writing.model.TextBlockLineTest;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxStateTest;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		PlanetaryCalendarTest.class, 
		CamTest.class,
		NotificationQueueTest.class,
		ChronometerTest.class,
		TextBlockBoxTest.class,
		TextBlockLineTest.class,
		WritingTextBoxStateTest.class,
		WritingTextBoxTest.class
	}
)
public class TestSuite {

}
