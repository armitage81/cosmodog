package antonafanasjew.cosmodog.ingamemenu.logplayer;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.ingamemenu.InGameMenuInputState;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogState;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;

public class LogPlayerInputState implements InGameMenuInputState {

	private GameLogs gameLogs;
	
	private int seriesNumber = 0;
	private int logNumber = 0;
	
	private int pages;
	private int currentPage;

	public LogPlayerInputState() {
		gameLogs = ApplicationContext.instance().getGameLogs();
	}
	
	
	public void left() {
		logNumber -= 1;
		if (logNumber < 0) {
			logNumber = 0;
		}
		recalculatePages();
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
		recalculatePages();
	}
	
	public void right() {
		logNumber = (logNumber + 1);
		String seriesForNumber = gameLogs.getSeriesNames().get(seriesNumber);
		List<GameLog> logsForSeries = gameLogs.getGameLogsForSeries(seriesForNumber);
		if (logNumber >= logsForSeries.size()) {
			logNumber = logsForSeries.size() - 1;
		}
		recalculatePages();
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
		recalculatePages();
	}
	
	public void rotatePage() {
		if (currentPage < pages - 1) {
			currentPage++;
		} else {
			currentPage = 0;
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
		recalculatePages();
	}
	
	private void recalculatePages() {
		String seriesName = gameLogs.getSeriesNames().get(seriesNumber);
		List<GameLog> gameLogsForSeries = gameLogs.getGameLogsForSeries(seriesName);
		GameLog gameLog = gameLogsForSeries.get(logNumber);
		GameLogState gameLogState = new GameLogState(gameLog, new TextPageConstraints(Constants.LOG_PLAYER_TEXT_WIDTH, Constants.LOG_PLAYER_TEXT_HEIGHT), FontType.GameLogPlayer);
		this.pages = gameLogState.getPages();
		this.currentPage = gameLogState.getCurrentPage();
	}

	
	
	public int getPages() {
		return pages;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
}
