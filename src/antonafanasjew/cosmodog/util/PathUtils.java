package antonafanasjew.cosmodog.util;

public class PathUtils {

	private static final String USER_HOME = System.getProperty("user.home"); 
	private static final String GAME_SAVE_DIR = USER_HOME + "/antonafanasjew/cosmodog";
	private static final String SCORE_SAVE_DIR = USER_HOME + "/antonafanasjew/cosmodog/score";
	private static final String SCORE_SAVE_PATH = SCORE_SAVE_DIR + "/scorelist.bin";
	
	public static String gameSaveDir() {
		return GAME_SAVE_DIR;
	}
	
	public static String scoreSaveDir() {
		return SCORE_SAVE_DIR;
	}
	
	public static String scoreSavePath() {
		return SCORE_SAVE_PATH;
	}
	
}
