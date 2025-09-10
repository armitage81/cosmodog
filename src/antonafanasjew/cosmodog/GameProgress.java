package antonafanasjew.cosmodog;

import java.io.Serial;
import java.util.Map;

import antonafanasjew.cosmodog.domains.QuadrandType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogModel;
import antonafanasjew.cosmodog.model.LetterPlateSequence;

import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import com.google.common.collect.Maps;

public class GameProgress extends CosmodogModel {

	@Serial
	private static final long serialVersionUID = 3139674682745482519L;

	public static final int TURNS_TILL_WORM_APPEARS_PHASE1 = 4;
	public static final int TURNS_TILL_WORM_APPEARS_PHASE2 = 6;
	public static final int TURNS_TILL_WORM_APPEARS_PHASE3 = 8;

	public static final String GAME_PROGRESS_PROPERTY_AFTERLANDING = "afterlanding";
	public static final String GAME_PROGRESS_ALIEN_BASE_GATE_SEQUENCE = "alienbasegatesequence";
	public static final String GAME_PROGRESS_ALIEN_BASE_TELEPORT_SEQUENCE = "alienbaseteleportsequence";
	public static final String GAME_PROGRESS_PROPERTY_ACCESSTOCARCOMPARTMENT = "accesstocarcompartment";

	private int turn = 0;

	private long gameScore;
	private boolean won = false;
	private int numberOfDeaths = 0;
	private int numberOfFoundSecrets = 0;


	private int infobits = 0;
	private int armors = 0;
	private int fuelTanks = 0;
	private int bottles = 0;
	private int foodCompartments = 0;
	private int soulEssences = 0;


	private int turnsTillWormAppears = TURNS_TILL_WORM_APPEARS_PHASE1;
	private boolean wormActive = true;
	
	//This information is redundand, as the mine deactivation will be stored in the
	//state of each mine. Still, we can use these information for purposes like game statistics.
	private final Map<QuadrandType, Boolean> minesDeactivationInfo = Maps.newHashMap();
	
	{
		minesDeactivationInfo.put(QuadrandType.NW, Boolean.FALSE);
		minesDeactivationInfo.put(QuadrandType.NE, Boolean.FALSE);
		minesDeactivationInfo.put(QuadrandType.SW, Boolean.FALSE);
		minesDeactivationInfo.put(QuadrandType.SE, Boolean.FALSE);
	}
	
	private final LetterPlateSequence letterPlateSequence = LetterPlateSequence.getInstance();
	
	private final Map<String, String> progressProperties = Maps.newHashMap();

	public long getGameScore() {
		return gameScore;
	}

	public void addToScore(int scorePoints) {
		this.gameScore += scorePoints;
	}

	public Map<String, String> getProgressProperties() {
		return progressProperties;
	}

	public void addInfobit() {
		infobits++;
	}

	public void addInfobyte() {
		infobits += 5;
	}
	
	public void addInfobank() {
		infobits += 25;
	}
	
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

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public int getNumberOfFoundSecrets() {
		return numberOfFoundSecrets;
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
	
	public void incTurn() {
		this.turn = this.turn + 1;
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
