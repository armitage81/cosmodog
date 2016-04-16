package antonafanasjew.cosmodog.model.actors;

import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.inventory.Inventory;

public class Player extends Actor {

	public static final int MAX_HUNGER = 5000;
	public static final int MAX_THIRST = 2500;
	
	
	public static final int MAX_POSSIBLE_LIFE = 50;
	public static final int INITIAL_LIFE = 10;
	public static final int LIFE_UNITS_IN_LIFEPACK = 10;

	private static final long serialVersionUID = 2286108151154430847L;

	private GameProgress gameProgress = new GameProgress();
	private Inventory inventory = new Inventory();

	private int hunger = 0;
	private int thirst = 0;

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

	public int getHunger() {
		return hunger;
	}

	public void setHunger(int n) {
		this.hunger = n;
		if (hunger > MAX_HUNGER) {
			hunger = MAX_HUNGER;
		}
	}

	public void increaseHunger(int n) {
		setHunger(getHunger() + n);
	}

	public boolean starving() {
		return hunger >= MAX_HUNGER;
	}

	public int getThirst() {
		return thirst;
	}

	public void setThirst(int n) {
		this.thirst = n;
		if (thirst >= MAX_THIRST) {
			thirst = MAX_THIRST;
		}
	}
	
	public void increaseThirst(int n) {
		setThirst(getThirst() + n);
	}

	public boolean dehydrating() {
		return thirst >= MAX_THIRST;
	}


}
