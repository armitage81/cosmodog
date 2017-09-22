package antonafanasjew.cosmodog.model.gamelog;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;


public class GameLogs {

	public static final String SERIES_UNSORTED = "unsorted";
	public static final String SERIES_MEMORIES = "memories";
	public static final String SERIES_CUTSCENES = "cutscenes";
	public static final String SERIES_MARYHARPER = "maryharper";
	public static final String SERIES_ALIENNOMADS = "aliennomads";
	
	public static final Map<String, Integer> SERIES_SORT_PRIORITIES = Maps.newHashMap();
	
	static {
		SERIES_SORT_PRIORITIES.put(SERIES_MEMORIES, 10);
		SERIES_SORT_PRIORITIES.put(SERIES_MARYHARPER, 20);
		SERIES_SORT_PRIORITIES.put(SERIES_ALIENNOMADS, 30);
		SERIES_SORT_PRIORITIES.put(SERIES_CUTSCENES, 40);
		SERIES_SORT_PRIORITIES.put(SERIES_UNSORTED, 90);
	}
	
	public static final Set<String> SPECIFIC_LOGS_SERIES = Sets.newHashSet(SERIES_UNSORTED, SERIES_CUTSCENES);
	
	private Multimap<String, GameLog> gameLogs = ArrayListMultimap.create();
	
	public void addGameLog(GameLog gameLog) {
		gameLogs.put(gameLog.getCategory(), gameLog);
	}
	
	public List<GameLog> getGameLogsForSeries(String series) {
		return Lists.newArrayList(gameLogs.get(series));
	}
	
	
	public GameLog getGameLogBySeriesAndId(String series, String id) {
		Collection<GameLog> gameLogSeries = gameLogs.get(series);
		for (GameLog gameLog : gameLogSeries) {
			if (gameLog.getIdInCategory().equals(id)) {
				return gameLog;
			}
		}
		
		throw new IllegalArgumentException("No game log with the id " + id + " was found in the series " + series + ".");
	}
	
	
	public GameLog getUnsortedGameLogById(String series, String id) {
		List<GameLog> unsortedGameLogs = Lists.newArrayList(gameLogs.get(series));
		for (GameLog gameLog : unsortedGameLogs) {
			if (gameLog.getIdInCategory().equals(id)) {
				return gameLog;
			}
		}
		
		throw new IllegalArgumentException("No unsorted game log found with the id " + id);
	}
	
	public Short getGameLogNumberById(String series, String id) {
		List<GameLog> unsortedGameLogs = Lists.newArrayList(gameLogs.get(series));
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
				
				String s1 = o1;
				String s2 = o2;

				Integer i1 = Optional.fromNullable(SERIES_SORT_PRIORITIES.get(s1)).or(50);
				Integer i2 = Optional.fromNullable(SERIES_SORT_PRIORITIES.get(s2)).or(50);
				
				return i1.compareTo(i2);
			}
		}); 
		
		return sortedCategories;
	}
	
}
