package antonafanasjew.cosmodog.model.inventory;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Represents the log player that contains all collected logs by the player.
 */
public class LogPlayer implements Serializable {

	private static final long serialVersionUID = 7333982356867336891L;
	
	//Saves the maximum number of found logs for each series, e.g 'AlienCaptainLog' => 5 means that 5 of the alien captains logs have been found already.
	private Map<String, Short> collectedLogsFromSeries = Maps.newHashMap();
	
	//Saves specific logs by their ids.
	private Set<Short> collectedSpecificLogs = Sets.newHashSet();

	public short noOfFoundLogsForSeries(String series) {
		Short no = collectedLogsFromSeries.get(series);
		no = no == null ? 0 : no;
		return no;
	}
	
	public void addLogToSeries(String series) {
		short currentNoOfLogsInSeries = noOfFoundLogsForSeries(series);
		collectedLogsFromSeries.put(series, (short)(currentNoOfLogsInSeries + 1));
	}
	
	public void addSpecificLog(Short logNumber) {
		collectedSpecificLogs.add(logNumber);
	}
	
	public Map<String, Short> allFoundLogsFromSeries() {
		return collectedLogsFromSeries;
	}
	
	public Set<Short> allFoundSpecificLogs() {
		return collectedSpecificLogs;
	}
	
}
