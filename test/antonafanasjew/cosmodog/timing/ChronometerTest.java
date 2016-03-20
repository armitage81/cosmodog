package antonafanasjew.cosmodog.timing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ChronometerTest {

	Chronometer c;
	
	@Before
	public void before() {
		c = new Chronometer();
	}
	
	@Test
	public void testTimeFrames() throws Exception {
		c.update(9);
		int timeFrame = c.timeFrameInLoop(2, 5);
		assertEquals(1, timeFrame);
		c.update(5);
		timeFrame = c.timeFrameInLoop(2, 5);
		assertEquals(0, timeFrame);
	}
}
