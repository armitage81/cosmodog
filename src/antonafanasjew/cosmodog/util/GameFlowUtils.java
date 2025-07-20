package antonafanasjew.cosmodog.util;

import java.io.File;
import java.util.Date;

import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.filesystem.CosmodogPersistenceException;
import antonafanasjew.cosmodog.filesystem.CosmodogScorePersistor;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.ScoreEntry;
import antonafanasjew.cosmodog.model.ScoreList;
import antonafanasjew.cosmodog.model.actors.Player;

public class GameFlowUtils {

	/**
	 * Call this after the game session has been finished (both loss and win case)
	 * to update and persist the game score list that is shared between sessions
	 */
	public static void updateScoreList() {
		Cosmodog cosmodog = ApplicationContext.instance().getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
		GameProgress gameProgress = player.getGameProgress();
		long lastGameScore = gameProgress.getGameScore();
		int starScore = gameProgress.starScore();
		ScoreEntry scoreEntry = new ScoreEntry(new Date(), lastGameScore, starScore);
		ScoreList scoreList = cosmodog.getScoreList();
		scoreList.addNewScoreEntry(scoreEntry);
		try {
			cosmodog.getScorePersistor().saveScoreList(scoreList, PathUtils.scoreSavePath());
		} catch (CosmodogPersistenceException e) {
			Log.error("Could not save score list", e);
		}
		
	}
	
	/**
	 * Don't call this while initializing the application context object to avoid the cycle 
	 */
	public static void loadScoreList() {
		Cosmodog cosmodog = ApplicationContext.instance().getCosmodog();
		CosmodogScorePersistor scorePersistor = cosmodog.getScorePersistor();
		ScoreList scoreList = ScoreList.getInstance();
		try {
			File f = new File(PathUtils.scoreSavePath());
			if (f.exists()) {
				scoreList = scorePersistor.restoreScoreList(PathUtils.scoreSavePath());
			}
		} catch (CosmodogPersistenceException e) {
			Log.error("Could not load score list", e);
		}
		cosmodog.setScoreList(scoreList);
	}
	
}
