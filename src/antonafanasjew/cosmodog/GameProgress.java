package antonafanasjew.cosmodog;

import java.util.Map;

import antonafanasjew.cosmodog.domains.QuadrandType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogModel;
import antonafanasjew.cosmodog.model.LetterPlateSequence;

import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import com.google.common.collect.Maps;

/**
 * Defines the complete game progress from the perspective of the player character.
 */
public class GameProgress extends CosmodogModel {

	public static final int NUMBER_OF_SECRETS_IN_GAME = 120;
	
	public static final int TURNS_TILL_WORM_APPEARS_PHASE1 = 4;
	public static final int TURNS_TILL_WORM_APPEARS_PHASE2 = 6;
	public static final int TURNS_TILL_WORM_APPEARS_PHASE3 = 8;
	
	private static final long serialVersionUID = 3139674682745482519L;

	/**
	 * The name of the game property that defines the happening of the 'after landing' event at the beginning of the game.
	 */
	public static final String GAME_PROGRESS_PROPERTY_AFTERLANDING = "afterlanding";
	public static final String GAME_PROGRESS_ALIEN_BASE_GATE_SEQUENCE = "alienbasegatesequence";
	public static final String GAME_PROGRESS_ALIEN_BASE_TELEPORT_SEQUENCE = "alienbaseteleportsequence";

	
	private boolean won = false;
	private int numberOfDeaths = 0;
	private int infobits = 0;
	private int armors = 0;
	private int fuelTanks = 0;
	private int bottles = 0;
	private int foodCompartments = 0;
	private int soulEssences = 0;
	private long gameScore;
	private int turn = 0;

	private int numberOfFoundSecrets = 0;
	
	private int turnsTillWormAppears = TURNS_TILL_WORM_APPEARS_PHASE1;
	private boolean wormActive = true;
	
	//This information is redundand, as the mine deactivation will be stored in the
	//state of each mine. Still, we can use these information for purposes like game statistics.
	private Map<QuadrandType, Boolean> minesDeactivationInfo = Maps.newHashMap();
	
	{
		minesDeactivationInfo.put(QuadrandType.NW, Boolean.FALSE);
		minesDeactivationInfo.put(QuadrandType.NE, Boolean.FALSE);
		minesDeactivationInfo.put(QuadrandType.SW, Boolean.FALSE);
		minesDeactivationInfo.put(QuadrandType.SE, Boolean.FALSE);
	}
	
	private LetterPlateSequence letterPlateSequence = LetterPlateSequence.getInstance();
	
	private Map<String, String> progressProperties = Maps.newHashMap();

	/**
	 * Returns the current game score.
	 * @return Game score.
	 */
	public long getGameScore() {
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
	
	public boolean isWormActive() {
		return wormActive;
	}
	
	public void setWormActive(boolean wormActive) {
		this.wormActive = wormActive;
	}

	public Map<QuadrandType, Boolean> getMinesDeactivationInfo() {
		return minesDeactivationInfo;
	}

	public void setMinesDeactivationInfo(Map<QuadrandType, Boolean> minesDeactivationInfo) {
		this.minesDeactivationInfo = minesDeactivationInfo;
	}

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public int getNumberOfFoundSecrets() {
		return numberOfFoundSecrets;
	}
	
	public void setNumberOfFoundSecrets(int numberOfFoundSecrets) {
		this.numberOfFoundSecrets = numberOfFoundSecrets;
	}
	
	public void increaseNumberOfFoundSecrets() {
		this.numberOfFoundSecrets += 1;
	}
	
	public int getArmors() {
		return armors;
	}
	
	public void setArmors(int armors) {
		this.armors = armors;
	}

	public int getFuelTanks() {
		return fuelTanks;
	}

	public void setFuelTanks(int fuelTanks) {
		this.fuelTanks = fuelTanks;
	}

	public int getBottles() {
		return bottles;
	}

	public void setBottles(int bottles) {
		this.bottles = bottles;
	}

	public int getFoodCompartments() {
		return foodCompartments;
	}

	public void setFoodCompartments(int foodCompartments) {
		this.foodCompartments = foodCompartments;
	}

	public int getSoulEssences() {
		return soulEssences;
	}
	
	public void setSoulEssences(int soulEssences) {
		this.soulEssences = soulEssences;
	}

	public void increaseArmors() {
		this.armors++;
	}

	public void increaseFuelTanks() {
		this.fuelTanks++;
	}

	public void increaseBottles() {
		this.bottles++;
	}

	public void increaseFoodCompartments() {
		this.foodCompartments++;
	}

	public void increaseSoulEssenses() {
		this.soulEssences++;
	}

	public LetterPlateSequence getLetterPlateSequence() {
		return letterPlateSequence;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public int incTurn() {
		this.turn = this.turn + 1;
		return this.turn;
	}

	public int getNumberOfDeaths() {
		return numberOfDeaths;
	}

	public void incNumberOfDeaths() {
		numberOfDeaths++;
	}

	public int starScore() {
		int starScore = 0;
		if(isWon()) {
			starScore++;
		}
		if (getInfobits() == Constants.NUMBER_OF_INFOBITS_IN_GAME) {
			starScore++;
		}
		if (getNumberOfFoundSecrets() == Constants.NUMBER_OF_SECRETS_IN_GAME) {
			starScore++;
		}

		int enemiesLeft = ApplicationContextUtils.getCosmodogGame()
				.getMaps()
				.values()
				.stream()
				.mapToInt(cosmodogMap -> cosmodogMap.allEnemies()
						.size())
				.sum();

		if (enemiesLeft == 0) {
			starScore++;
		}
		if (isWon() && getNumberOfDeaths() == 0) {
			starScore++;
		}
		return starScore;
	}
}
