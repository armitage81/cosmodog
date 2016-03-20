package antonafanasjew.cosmodog;

import java.util.Map;

import antonafanasjew.cosmodog.model.CosmodogModel;

import com.google.common.collect.Maps;

public class GameProgress extends CosmodogModel {

	public static final String GAME_PROGRESS_PROPERTY_AFTERLANDING = "afterlanding";

	private static final long serialVersionUID = 3139674682745482519L;

	public static final int SCORE_PER_INFOBIT = 100;

	private int infobits = 0;
	private int gameScore;

	private Map<String, String> progressProperties = Maps.newHashMap();

	public int getGameScore() {
		return gameScore;
	}

	public void setGameScore(int gameScore) {
		this.gameScore = gameScore;
	}

	public void addToScore(int scorePoints) {
		this.gameScore += scorePoints;
	}

	/**
	 * Contains persistent game variables that can be set to indicate a progress
	 * (f.i. completed tutorial)
	 */
	public Map<String, String> getProgressProperties() {
		return progressProperties;
	}

	public void addInfobit() {
		infobits++;
	}
	
	public int getInfobits() {
		return infobits;
	}

}
