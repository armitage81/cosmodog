package antonafanasjew.cosmodog.model.gamelog;

import java.util.List;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;

public class GameLogState {

	private GameLog gameLog;
	private int pages;
	private int currentPage;
	
	public GameLogState(GameLog gameLog, TextPageConstraints c) {
		super();
		this.gameLog = gameLog;
		String text = gameLog.getLogText();
		
		//We don't care about the actual constraints.
		//The only relevant thing for the page number are the page break mark up tags in the text.
		List<List<String>> splitText = c.textSplitByLinesAndPages(text, FontType.GameLog.getFont());
		pages = splitText.size();
		
		this.currentPage = 0;
	}

	public GameLog getGameLog() {
		return gameLog;
	}

	public int getPages() {
		return pages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public boolean onLastPage() {
		return currentPage == pages - 1;
	}
	
	public void nextPage() {
		if (currentPage < pages - 1) {
			currentPage++;
		}
	}
	
}
