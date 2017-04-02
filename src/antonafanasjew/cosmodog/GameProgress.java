package antonafanasjew.cosmodog;

import java.util.Map;

import antonafanasjew.cosmodog.domains.QuadrandType;
import antonafanasjew.cosmodog.model.CosmodogModel;

import com.google.common.collect.Maps;

/**
 * Defines the complete game progress from the perspective of the player character.
 */
public class GameProgress extends CosmodogModel {
	
	public static final int TURNS_TILL_WORM_APPEARS_PHASE1 = 4;
	public static final int TURNS_TILL_WORM_APPEARS_PHASE2 = 6;
	public static final int TURNS_TILL_WORM_APPEARS_PHASE3 = 8;
	
	private static final long serialVersionUID = 3139674682745482519L;

	/**
	 * The name of the game property that defines the happening of the 'after landing' event at the beginning of the game.
	 */
	public static final String GAME_PROGRESS_PROPERTY_AFTERLANDING = "afterlanding";
	public static final String GAME_PROGRESS_ALIEN_BASE_GATE_SEQUENCE = "alienbasegatesequence";

	private int infobits = 0;
	private int gameScore;
	
	private int turnsTillWormAppears = TURNS_TILL_WORM_APPEARS_PHASE1;
	
	//This information is redundand, as the mine deactivation will be stored in the
	//state of each mine. Still, we can use these information for purposes like game statistics.
	private Map<QuadrandType, Boolean> minesDeactivationInfo = Maps.newHashMap();
	
	{
		minesDeactivationInfo.put(QuadrandType.NW, Boolean.FALSE);
		minesDeactivationInfo.put(QuadrandType.NE, Boolean.FALSE);
		minesDeactivationInfo.put(QuadrandType.SW, Boolean.FALSE);
		minesDeactivationInfo.put(QuadrandType.SE, Boolean.FALSE);
	}
	

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

	public void addInfobyte() {
		infobits += 5;
	}
	
	public void addInfobank() {
		infobits += 25;
	}
	
	/**
	 * Returns the number of infobits.
	 * @return Number of infobits.
	 */
	public int getInfobits() {
		return infobits;
	}

	public int getTurnsTillWormAppears() {
		return turnsTillWormAppears;
	}

	public void setTurnsTillWormAppears(int turnsTillWormAppears) {
		this.turnsTillWormAppears = turnsTillWormAppears;
	}

	public Map<QuadrandType, Boolean> getMinesDeactivationInfo() {
		return minesDeactivationInfo;
	}

	public void setMinesDeactivationInfo(Map<QuadrandType, Boolean> minesDeactivationInfo) {
		this.minesDeactivationInfo = minesDeactivationInfo;
	}
	
}
