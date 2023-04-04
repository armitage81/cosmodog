package antonafanasjew.cosmodog.timing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TimerTest {

	public static final int ONE_DAY = 24 * 3600 * 1000;
	public static final int ONE_HOUR = 3600 * 1000;
	public static final int ONE_MINUTE = 60 * 1000;
	public static final int ONE_SECOND = 1000;
	
	@Test
	void test() {
		
		long referenceTime = (long)(1 * ONE_DAY + 2 * ONE_HOUR + 3 * ONE_MINUTE + 4 * ONE_SECOND);
		
		Timer timer = new Timer();
		timer.setReferenceTimeSupplier(() -> 0L);
		timer.initLastUpdateTime();
		timer.initPlayTime();
		timer.setReferenceTimeSupplier(() -> referenceTime);
		timer.updatePlayTime();
		
		assertEquals(referenceTime, timer.getPlayTime());
		assertEquals(1, timer.getPlayTimeDays());
		assertEquals(2, timer.getPlayTimeHours());
		assertEquals(3, timer.getPlayTimeMinutes());
		assertEquals(4, timer.getPlayTimeSeconds());
		
		assertEquals("1 day(s), 2 hour(s), 3 minutes and 4 seconds", timer.playTimeRepresentationDaysHoursMinutesSeconds("%s day(s), %s hour(s), %s minutes and %s seconds"));
		assertEquals("1D:2H:3M", timer.playTimeRepresentationDaysHoursMinutes("%sD:%sH:%sM"));
		assertEquals("1 Tag(e) und 2 Stunde(n)", timer.playTimeRepresentationDaysHours("%s Tag(e) und %s Stunde(n)"));
		
		
	}

}
