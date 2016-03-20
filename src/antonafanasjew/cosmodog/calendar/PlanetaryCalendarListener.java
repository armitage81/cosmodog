package antonafanasjew.cosmodog.calendar;

import java.io.Serializable;

/**
 * Listener of a planetary calendar.
 * It defines a method that can be implemented to react on time updates on the planetary calendar.
 */
public interface PlanetaryCalendarListener extends Serializable {

	/**
	 * Defines a method that will be called in case of time updates on a planetary calendar. 
	 * 
	 * @param fromTimeInMinutes Initial state of the calendar before the passed time applies (in minutes). 
	 * @param noOfMinutes Passed time in minutes as related to the calendars time stamp.
	 */
	public void timePassed(long fromTimeInMinutes, int noOfMinutes);
	
}
