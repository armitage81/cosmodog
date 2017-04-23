package antonafanasjew.cosmodog.model.gamelog;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;


public class GameLogs {

	public static final String SPECIFIC_LOGS_SERIES = "unsorted";
	
	private Multimap<String, GameLog> gameLogs = ArrayListMultimap.create();
	
	public void addGameLog(GameLog gameLog) {
		gameLogs.put(gameLog.getCategory(), gameLog);
	}
	
	public List<GameLog> getGameLogsForSeries(String series) {
		return Lists.newArrayList(gameLogs.get(series));
	}
	
	public GameLog getUnsortedGameLogById(String id) {
		List<GameLog> unsortedGameLogs = Lists.newArrayList(gameLogs.get(SPECIFIC_LOGS_SERIES));
		for (GameLog gameLog : unsortedGameLogs) {
			if (gameLog.getIdInCategory().equals(id)) {
				return gameLog;
			}
		}
		
		throw new IllegalArgumentException("No unsorted game log found with the id " + id);
	}
	
	public Short getUnsortedGameLogNumberById(String id) {
		List<GameLog> unsortedGameLogs = Lists.newArrayList(gameLogs.get(SPECIFIC_LOGS_SERIES));
		for (short i = 0; i < unsortedGameLogs.size(); i++) {
			GameLog gameLog = unsortedGameLogs.get(i);
			if (gameLog.getIdInCategory().equals(id)) {
				return i;
			}
		}
		
		throw new IllegalArgumentException("No unsorted game log found with the id " + id);
	}
	
	public List<String> getSeriesNames() {
		List<String> sortedCategories = Lists.newArrayList(gameLogs.keySet());
		
		Collections.sort(sortedCategories, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				
				if (SPECIFIC_LOGS_SERIES.equals(o1) && SPECIFIC_LOGS_SERIES.equals(o2)) {
					return 0;
				} else if (SPECIFIC_LOGS_SERIES.equals(o1)) {
					return -1;
				} else if (SPECIFIC_LOGS_SERIES.equals(o2)) {
					return 1;
				}
				
				return o1.compareTo(o2);
			}
		}); 
		
		return sortedCategories;
	}
	
}
