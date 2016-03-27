package antonafanasjew.cosmodog;

import java.util.Map;

import antonafanasjew.cosmodog.model.CosmodogModel;

import com.google.common.collect.Maps;

/**
 * Defines the complete game progress from the perspective of the player character.
 */
public class GameProgress extends CosmodogModel {

	private static final long serialVersionUID = 3139674682745482519L;

	/**
	 * The name of the game property that defines the happening of the 'after landing' event at the beginning of the game.
	 */
	public static final String GAME_PROGRESS_PROPERTY_AFTERLANDING = "afterlanding";

	private int infobits = 0;
	private int gameScore;

	private Map<String, String> progressProperties = Maps.newHashMap();

	/**
	 * Returns the current game score.
	 * @return Game score.
	 */
	public int getGameScore() {
		return gameScore;
	}

	/**
	 * Sets the current game score.
	 * @param gameScore Game score.
	 */
	public void setGameScore(int gameScore) {
		this.gameScore = gameScore;
	}

	/**
	 * Adds a number of score points to the current game score.
	 * @param scorePoints Number of score points to add to the game score.
	 */
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

	/**
	 * Adds 1 to the number of collected infobits.
	 */
	public void addInfobit() {
		infobits++;
	}
	
	/**
	 * Returns the number of infobits.
	 * @return Number of infobits.
	 */
	public int getInfobits() {
		return infobits;
	}

}
