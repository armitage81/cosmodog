package antonafanasjew.cosmodog.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PlanetaryCalendarTest {

	PlanetaryCalendar c = new PlanetaryCalendar();
	
	@Test
	public void testMinutes() throws Exception {
		c.addMinutes(0);
		assertEquals(0, c.getMinute());
		assertEquals(0, c.getHour());
		assertEquals(0, c.getDay());
		assertEquals(0, c.getMonth());
		assertEquals(0, c.getYear());
		
		c.addMinutes(1);
		
		assertEquals(1, c.getMinute());
		assertEquals(0, c.getHour());
		assertEquals(0, c.getDay());
		assertEquals(0, c.getMonth());
		assertEquals(0, c.getYear());
	}
	
	@Test
	public void testHours() throws Exception {
		c.addMinutes(PlanetaryCalendar.MINUTES_IN_HOUR);
		assertEquals(0, c.getMinute());
		assertEquals(1, c.getHour());
		assertEquals(0, c.getDay());
		assertEquals(0, c.getMonth());
		assertEquals(0, c.getYear());
		
		c.addMinutes(PlanetaryCalendar.MINUTES_IN_HOUR + 1 );
		assertEquals(1, c.getMinute());
		assertEquals(2, c.getHour());
		assertEquals(0, c.getDay());
		assertEquals(0, c.getMonth());
		assertEquals(0, c.getYear());
		
		c.addMinutes(PlanetaryCalendar.MINUTES_IN_HOUR - 1);
		assertEquals(0, c.getMinute());
		assertEquals(3, c.getHour());
		assertEquals(0, c.getDay());
		assertEquals(0, c.getMonth());
		assertEquals(0, c.getYear());
		
		c.addMinutes(2 * PlanetaryCalendar.MINUTES_IN_HOUR + 10);
		assertEquals(10, c.getMinute());
		assertEquals(5, c.getHour());
		assertEquals(0, c.getDay());
		assertEquals(0, c.getMonth());
		assertEquals(0, c.getYear());
	}
	
	@Test
	public void testDays() throws Exception {
		c.addMinutes(PlanetaryCalendar.MINUTES_IN_HOUR * PlanetaryCalendar.HOURS_IN_DAY);
		assertEquals(0, c.getMinute());
		assertEquals(0, c.getHour());
		assertEquals(1, c.getDay());
		assertEquals(0, c.getMonth());
		assertEquals(0, c.getYear());
		
		c.addMinutes(15 + 5 * PlanetaryCalendar.MINUTES_IN_HOUR + 10 * PlanetaryCalendar.MINUTES_IN_HOUR * PlanetaryCalendar.HOURS_IN_DAY);
		assertEquals(15, c.getMinute());
		assertEquals(5, c.getHour());
		assertEquals(11, c.getDay());
		assertEquals(0, c.getMonth());
		assertEquals(0, c.getYear());
		
	}
	
	@Test
	public void testMonths() throws Exception {
		c.addMinutes(1 + 2 * PlanetaryCalendar.MINUTES_IN_HOUR  + 3 * PlanetaryCalendar.MINUTES_IN_HOUR * PlanetaryCalendar.HOURS_IN_DAY + 4 * PlanetaryCalendar.MINUTES_IN_HOUR * PlanetaryCalendar.HOURS_IN_DAY * PlanetaryCalendar.DAYS_IN_MONTH);
		assertEquals(1, c.getMinute());
		assertEquals(2, c.getHour());
		assertEquals(3, c.getDay());
		assertEquals(4, c.getMonth());
		assertEquals(0, c.getYear());
		
	}
	
}
