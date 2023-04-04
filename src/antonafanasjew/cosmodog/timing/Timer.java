package antonafanasjew.cosmodog.timing;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * This class counts game time.
 * It consists of two parts: The serializable playTime and the transient lastUpdateTime.
 * 
 * lastUpdateTime is initialized with the value of the current time (referenceTime) when a new game is started or a previously stored game is restored.
 * Additionally, it is set to referenceTime each time the playTime is updated.
 * lastUpdateTime is not being persisted.
 * 
 * playTime is set to 0 when a new game is started. It is set to the stored value when the game is restored. 
 * It can be stored together with the remaining game state.
 * It can be updated in irregular intervals by setting it to playTime := playTime + (referenceTime - lastUpdateTime).
 * After each such update, lastUpdateTime must be set to referenceTime.
 * 
 */
public class Timer implements Serializable {

	private static final long serialVersionUID = -5213503704477172402L;

	private transient Supplier<Long> referenceTimeSupplier = () -> System.currentTimeMillis();
	
	private transient long lastUpdateTime;
	private long playTime;
	
	
	public void setReferenceTimeSupplier(Supplier<Long> referenceTimeSupplier) {
		this.referenceTimeSupplier = referenceTimeSupplier;
	}
	
	public void initLastUpdateTime() {
		lastUpdateTime = referenceTimeSupplier.get();
	}
	
	public void initPlayTime() {
		playTime = 0;
	}
	
	public void updatePlayTime() {
		long referenceTime = referenceTimeSupplier.get();
		playTime = playTime + referenceTime - lastUpdateTime;
		lastUpdateTime = referenceTime;
	}
	
	public long getPlayTime() {
		return playTime;
	}
	
	public short getPlayTimeSeconds() {
		int milliesOnLastMinute = (int)(playTime % (1000 * 60));
		return (short)(milliesOnLastMinute / 1000);
	}
	
	public short getPlayTimeMinutes() {
		int milliesOnLastHour = (int)(playTime % (1000 * 3600));
		return (short)(milliesOnLastHour / (1000 * 60));
	}
	
	public short getPlayTimeHours() {
		int milliesOnLastDay = (int)(playTime % (1000 * 3600 * 24));
		return (short)(milliesOnLastDay / (1000 * 3600));
	}
	
	public short getPlayTimeDays() {
		return (short)(playTime / (1000 * 3600 * 24)); 
	}
	
	public String playTimeRepresentationDaysHoursMinutesSeconds(String formatString) {
		return String.format(formatString, getPlayTimeDays(), getPlayTimeHours(), getPlayTimeMinutes(), getPlayTimeSeconds());
	}
	
	public String playTimeRepresentationDaysHoursMinutes(String formatString) {
		return String.format(formatString, getPlayTimeDays(), getPlayTimeHours(), getPlayTimeMinutes());
	}
	
	public String playTimeRepresentationDaysHours(String formatString) {
		return String.format(formatString, getPlayTimeDays(), getPlayTimeHours());
	}
	
	
	
	
}
