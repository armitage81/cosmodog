package antonafanasjew.cosmodog.ingamemenu.logplayer;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.ingamemenu.InGameMenuInputState;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;

public class LogPlayerInputState implements InGameMenuInputState {

	private GameLogs gameLogs;
	
	private int seriesNumber = 0;
	private int logNumber = 0;
	
	private Book currentLogBook;

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
		if (!currentLogBook.onLastPage()) {
			currentLogBook.nextPage();
		} else {
			currentLogBook.firstPage();
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
		String text = gameLog.getLogText();
		DrawingContext textDc = DrawingContextProviderHolder.get().getDrawingContextProvider().logPlayerTextDrawingContext();
		TextPageConstraints tpc = TextPageConstraints.fromDc(textDc);
		currentLogBook = tpc.textToBook(text, FontRefToFontTypeMap.forNarration(), 0);
	}

	
	
	public Book getCurrentLogBook() {
		return currentLogBook;
	}
	
	public void setCurrentLogBook(Book currentLogBook) {
		this.currentLogBook = currentLogBook;
	}
}
