package antonafanasjew.cosmodog.ingamemenu.logplayer;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.ingamemenu.InGameMenuInputState;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;

public class LogPlayerInputState implements InGameMenuInputState {

	private GameLogs gameLogs;
	
	private int seriesNumber = 0;
	private int logNumber = 0;

	public LogPlayerInputState() {
		gameLogs = ApplicationContext.instance().getGameLogs();
	}
	
	
	public void left() {
		logNumber -= 1;
		if (logNumber < 0) {
			logNumber = 0;
		}
	}
	
	public void up() {
		seriesNumber -= 1;
		if (seriesNumber < 0) {
			seriesNumber = 0;
		}
		String seriesForNumber = gameLogs.getSeriesNames().get(seriesNumber);
		List<GameLog> logsForSeries = gameLogs.getGameLogsForSeries(seriesForNumber);
		if (logNumber >= logsForSeries.size()) {
			logNumber = logsForSeries.size() - 1;
		}
	}
	
	public void right() {
		logNumber = (logNumber + 1);
		String seriesForNumber = gameLogs.getSeriesNames().get(seriesNumber);
		List<GameLog> logsForSeries = gameLogs.getGameLogsForSeries(seriesForNumber);
		if (logNumber >= logsForSeries.size()) {
			logNumber = logsForSeries.size() - 1;
		}
	}
	
	public void down() {
		int numberOfSeries = gameLogs.getSeriesNames().size();
		seriesNumber = (seriesNumber + 1);
		if (seriesNumber >= numberOfSeries) {
			seriesNumber = numberOfSeries - 1;
		}
		String seriesForNumber = gameLogs.getSeriesNames().get(seriesNumber);
		List<GameLog> logsForSeries = gameLogs.getGameLogsForSeries(seriesForNumber);
		if (logNumber >= logsForSeries.size()) {
			logNumber = logsForSeries.size() - 1;
		}
	}
	
	public int getLogNumber() {
		return logNumber;
	}

	public int getSeriesNumber() {
		return seriesNumber;
	}


	@Override
	public void initializeState() {
		seriesNumber = 0;
		logNumber = 0;
	}
}
