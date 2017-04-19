package antonafanasjew.cosmodog.model.actors;

import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.Inventory;

public class Player extends Actor {

	public static final int INITIAL_MAX_FOOD = 600;
	public static final int INITIAL_MAX_WATER = 600;
	
//	public static final int INITIAL_MAX_FOOD = 20;
//	public static final int INITIAL_MAX_WATER = 30;
	
	public static final int MAX_POSSIBLE_LIFE = 50;
	public static final int INITIAL_LIFE = 10;
	public static final int LIFE_UNITS_IN_LIFEPACK = 5;

	private static final long serialVersionUID = 2286108151154430847L;

	private GameProgress gameProgress = new GameProgress();
	private Inventory inventory = new Inventory();
	private Arsenal arsenal = new Arsenal();

	private int currentMaxFood = INITIAL_MAX_FOOD;
	private int currentMaxWater = INITIAL_MAX_WATER;
	
	private int food = currentMaxFood;
	private int water = currentMaxWater;
	
	private int turnsWormAlerted = 0;
	
	private boolean poisoned = false;
	
	private int turnsPoisoned = 0;
	
	public static Player fromPosition(int positionX, int positionY) {
		Player player = new Player();
		player.setMaxLife(INITIAL_LIFE);
		player.setLife(player.getMaxLife());
		player.setPositionX(positionX);
		player.setPositionY(positionY);
		player.setDirection(DirectionType.RIGHT);
		return player;
	}

	public GameProgress getGameProgress() {
		return gameProgress;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Arsenal getArsenal() {
		return arsenal;
	}
	
	public int getFood() {
		return food;
	}

	public void setFood(int n) {
		this.food = n;
		if (food > currentMaxFood) {
			food = currentMaxFood;
		}
		
		if (food < 0) {
			food = 0;
		}
	}

	public void decreaseFood(int n) {
		setFood(getFood() - n);
	}

	public boolean starving() {
		return food <= 0;
	}

	public int getWater() {
		return water;
	}

	public void setWater(int n) {
		this.water = n;
		if (water >= currentMaxWater) {
			water = currentMaxWater;
		}
		if (water < 0) {
			water = 0;
		}
	}
	
	public int getCurrentMaxFood() {
		return currentMaxFood;
	}

	public void setCurrentMaxFood(int currentMaxFood) {
		this.currentMaxFood = currentMaxFood;
	}

	public int getCurrentMaxWater() {
		return currentMaxWater;
	}

	public void setCurrentMaxWater(int currentMaxWater) {
		this.currentMaxWater = currentMaxWater;
	}
	
	public void decreaseWater(int n) {
		setWater(getWater() - n);
	}

	public boolean dehydrating() {
		return water <= 0;
	}

	public int getTurnsWormAlerted() {
		return turnsWormAlerted;
	}

	public void increaseTurnsWormAlerted() {
		this.turnsWormAlerted++;
	}
	
	public void resetTurnsWormAlerted() {
		this.turnsWormAlerted = 0;
	}
	
	public int getTurnsPoisoned() {
		return turnsPoisoned;
	}

	public void increaseTurnsPoisoned() {
		if (poisoned) {
			this.turnsPoisoned++;
		}
	}

	public void contaminate() {
		this.poisoned = true;
	}
	
	public void decontaminate() {
		this.poisoned = false;
		this.turnsPoisoned = 0;
	}
	
	public boolean isPoisoned() {
		return poisoned;
	}

	
}
