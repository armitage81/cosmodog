package antonafanasjew.cosmodog.model.gamelog;

public class GameLog {
	
	private String category;
	private String idInCategory;
	private String header;
	private String logText;
	
	public static GameLog instance(String category, String idInCategory, String header, String logText) {
		GameLog gameLog = new GameLog();
		gameLog.category = category;
		gameLog.idInCategory = idInCategory;
		gameLog.header = header;
		gameLog.logText = logText;
		return gameLog;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getIdInCategory() {
		return idInCategory;
	}

	public String getHeader() {
		return header;
	}

	public String getLogText() {
		return logText;
	}

}
