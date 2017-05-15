package antonafanasjew.cosmodog.calendar;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Definition of a custom (and imaginary) calendar.
 * 
 * It is based on minutes, hours, days and months. 
 * There are no leap units, no seconds and no exceptions to the time arithmetics.
 *
 * Take care: a day does not have necessarily 24 hours in this calendar. 
 * See also {@link #MINUTES_IN_HOUR}, {@link #HOURS_IN_DAY}, {@link #DAYS_IN_MONTH} and {@link #MONTHS_IN_YEAR} 
 * 
 * Take note: This calendar does not have a concept of weeks.
 */
public class PlanetaryCalendar implements Serializable {

	private static final long serialVersionUID = 6786253845602530454L;
	
	/**
	 * Defines the number of minutes in this calendars hour.
	 */
	public static final int MINUTES_IN_HOUR = 60;
	
	/**
	 * Defines the number of hours in this calendars day.
	 */
	public static final int HOURS_IN_DAY = 20;
	
	/**
	 * Defines the number of days in this calendars month.
	 */
	public static final int DAYS_IN_MONTH = 30;
	
	/**
	 * Defines the number of months in this calendars year.
	 */
	public static final int MONTHS_IN_YEAR = 10;
	
	/*
	 * This is used only to use the date format.
	 */
	private Calendar calendar = new GregorianCalendar();
	
	/*
	 * Date format for printing the calendar.
	 */
	private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
	
	private int year = 0;
	private int day = 0;
	private int hour = 0;
	private int month = 0;
	private int minute = 0;
	
	/**
	 * Returns the year.
	 * @return The calendars year.
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * Sets the calendars year. Take care, the year indexing starts with 0 and there are no negative years.
	 * @param year Year number.
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	 * Returns the month.
	 * @return This calendars month.
	 */
	public int getMonth() {
		return month;
	}
	
	/**
	 * Sets the calendars month. Take care, the month indexing starts with 0.. 
	 * @param month
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	
	/**
	 * Returns day.
	 * @return This calendars day.
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * Sets the day. Take care, the day indexing starts with 0.
	 * @param day
	 */
	public void setDay(int day) {
		this.day = day;
	}
	
	/**
	 * Returns the days hour (Between 0 and {@link #HOURS_IN_DAY} - 1).
	 * @return This calendars current day hour.
	 */
	public int getHour() {
		return hour;
	}
	
	/**
	 * Sets the hour.
	 * @param hour Hour Between 0 and {@link #HOURS_IN_DAY} - 1.
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	/**
	 * Returns the minute of the currently set hour.
	 * @return The minute in the currently set hour (Between 0 and {@link #MINUTES_IN_HOUR} - 1).
	 */
	public int getMinute() {
		return minute;
	}
	
	/**
	 * Sets the minute of the currently set hour. 
	 * @param minute Minute Between 0 and {@link #MINUTES_IN_HOUR} - 1.
	 */
	public void setMinute(int minute) {
		this.minute = minute;
	}

	/**
	 * Returns the time stamp of this calendar in minutes (as they are the smallest units).
	 * @return Time stamp in minutes.
	 */
	public long getTimeInMinutes() {
		return 
				this.minute + 
				this.hour * MINUTES_IN_HOUR + 
				this.day * HOURS_IN_DAY * MINUTES_IN_HOUR + 
				this.month * DAYS_IN_MONTH * HOURS_IN_DAY * MINUTES_IN_HOUR + 
				this.year * MONTHS_IN_YEAR * DAYS_IN_MONTH * HOURS_IN_DAY * MINUTES_IN_HOUR;
	}
	
	/**
	 * Sets the calendars time from the time stamp.
	 * 
	 * Take note: This method is for initialization only as it does not notify the listener.
	 * 
	 * @param timeInMinutes Time stamp as the number of passed minutes since the zero time.
	 */
	public void setTimeInMinutes(long timeInMinutes) {
		this.minute = (int)timeInMinutes % MINUTES_IN_HOUR;
		this.hour = (int)timeInMinutes / MINUTES_IN_HOUR % HOURS_IN_DAY;
		this.day = (int)timeInMinutes / MINUTES_IN_HOUR / HOURS_IN_DAY % DAYS_IN_MONTH;
		this.month = (int)timeInMinutes / MINUTES_IN_HOUR / HOURS_IN_DAY / DAYS_IN_MONTH % MONTHS_IN_YEAR;
		this.year = (int)timeInMinutes / MINUTES_IN_HOUR / HOURS_IN_DAY / DAYS_IN_MONTH / MONTHS_IN_YEAR;
	}
	
	/**
	 * Adds minutes to the calendars time and notifies the listener.
	 * @param minutes Number of minutes to add.
	 */
	public void addMinutes(int minutes) {
		long oldTime = getTimeInMinutes();
		long newTime = oldTime + minutes;
		setTimeInMinutes(newTime);
	}
	
	/**
	 * Returns the representation of this calendars time.
	 * @param locale Locale of the representation.
	 * @return Textual representation of this calendars time.
	 */
	public String toString(Locale locale) {
				
		StringBuffer sb = new StringBuffer();
		
		sb.append(year + 1).append(".");
		sb.append(month + 1).append(".");
		sb.append(day + 1).append(" ");
		
		calendar.set(Calendar.YEAR, 2000);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		
		sb.append(dateFormat.format(calendar.getTime()));
		return sb.toString();
	}
	
	public String toTimeString(Locale locale) {
		
		StringBuffer sb = new StringBuffer();
		
		calendar.set(Calendar.YEAR, 2000);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		
		sb.append(dateFormat.format(calendar.getTime()));
		return sb.toString();
	}

	/**
	 * Indicates whether this calendar is currently set to day time (Meaning, sun is high).
	 * @return true if it is day time, false otherwise.
	 */
	public boolean isDay() {
		return this.hour >= 8 && this.hour < 18;
	}
	
	/**
	 * Indicates whether this calendar is currently set to evening time (sunset).
	 * @return true if it is evening time, false otherwise.
	 */
	public boolean isEvening() {
		return this.hour >= 18 && this.hour < 20;
	}
	
	/**
	 * Indicates whether this calendar is currently set to night time (sun is down).
	 * @return true if it is night time, false otherwise.
	 */
	public boolean isNight() {
		return this.hour >= 0 && this.hour < 6;
	}
	
	/**
	 * Indicates whether this calendar is currently set to morning time (sunrise).
	 * @return true if it is morning time, false otherwise.
	 */
	public boolean isMorning() {
		return this.hour >= 6 && this.hour < 8;
	}
}
