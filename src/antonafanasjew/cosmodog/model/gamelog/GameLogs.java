package antonafanasjew.cosmodog.model.gamelog;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;


public class GameLogs {

	public static final String SPECIFIC_LOGS_SERIES = "unsorted";
	public static final String CUTSCENE_LOGS_SERIES = "cutscenes";
	
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
	
	
	public GameLog getUnsortedGameLogById(String id) {
		List<GameLog> unsortedGameLogs = Lists.newArrayList(gameLogs.get(SPECIFIC_LOGS_SERIES));
		for (GameLog gameLog : unsortedGameLogs) {
			if (gameLog.getIdInCategory().equals(id)) {
				return gameLog;
			}
		}
		
		throw new IllegalArgumentException("No unsorted game log found with the id " + id);
	}
	
	public GameLog getCutsceneById(String id) {
		List<GameLog> cutscenes = Lists.newArrayList(gameLogs.get(CUTSCENE_LOGS_SERIES));
		for (GameLog gameLog : cutscenes) {
			if (gameLog.getIdInCategory().equals(id)) {
				return gameLog;
			}
		}
		
		throw new IllegalArgumentException("No cutscene found with the id " + id);
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
	
	public Short getCutsceneNumberById(String id) {
		List<GameLog> cutscenes = Lists.newArrayList(gameLogs.get(CUTSCENE_LOGS_SERIES));
		for (short i = 0; i < cutscenes.size(); i++) {
			GameLog gameLog = cutscenes.get(i);
			if (gameLog.getIdInCategory().equals(id)) {
				return i;
			}
		}
		
		throw new IllegalArgumentException("No cutscene found with the id " + id);
	}
	
	
	public List<String> getSeriesNames() {
		List<String> sortedCategories = Lists.newArrayList(gameLogs.keySet());
		
		Collections.sort(sortedCategories, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				
				String s1 = o1;
				if (s1.equals(SPECIFIC_LOGS_SERIES)) {
					s1 = "%" + s1;
				}
				if (s1.equalsIgnoreCase(CUTSCENE_LOGS_SERIES)) {
					s1 = "%%" + s1;
				}
				
				String s2 = o2;
				if (s2.equals(SPECIFIC_LOGS_SERIES)) {
					s2 = "%" + s2;
				}
				if (s2.equalsIgnoreCase(CUTSCENE_LOGS_SERIES)) {
					s2 = "%%" + s2;
				}
				
				return o1.compareTo(o2);
			}
		}); 
		
		return sortedCategories;
	}
	
}
