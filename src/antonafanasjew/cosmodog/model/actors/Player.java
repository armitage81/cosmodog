package antonafanasjew.cosmodog.model.actors;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.listener.movement.PlayerMovementListener;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.LogPlayer;
import antonafanasjew.cosmodog.model.portals.Ray;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class Player extends Actor {

	public static final int INITIAL_MAX_FOOD = 600;
	public static final int INITIAL_MAX_WATER = 600;
	
//	public static final int INITIAL_MAX_FOOD = 20;
//	public static final int INITIAL_MAX_WATER = 30;
	
	public static final int MAX_POSSIBLE_LIFE = 100;
	public static final int INITIAL_LIFE = 10;
	public static final int LIFE_UNITS_IN_LIFEPACK = 5;
	public static final int ROBUSTNESS_UNITS_IN_ARMORPACK = 10;

	private static final long serialVersionUID = 2286108151154430847L;

	private GameProgress gameProgress = new GameProgress();
	private Inventory inventory = new Inventory();
	private Arsenal arsenal = new Arsenal();
	private LogPlayer logPlayer = new LogPlayer();

	private int currentMaxFood = INITIAL_MAX_FOOD;
	private int currentMaxWater = INITIAL_MAX_WATER;
	
	private int food = currentMaxFood;
	private int water = currentMaxWater;
	
	private int turnsWormAlerted = 0;
	
	private boolean poisoned = false;
	
	private int turnsPoisoned = 0;

	private Ray portalRay = null;

	private PlayerMovementListener movementListener = new PlayerMovementListener();

	public static Player fromPosition(Position position) {
		Player player = new Player();
		player.setMaxLife(INITIAL_LIFE);
		player.setLife(player.getMaxLife());
		player.setPosition(position);
		player.setDirection(DirectionType.RIGHT);
		return player;
	}

	public PlayerMovementListener getMovementListener() {
		return movementListener;
	}

	public void setMovementListener(PlayerMovementListener movementListener) {
		this.movementListener = movementListener;
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

	public LogPlayer getLogPlayer() {
		return logPlayer;
	}

	public void shiftHorizontal(int positionOffset) {
		Position position1 = this.getPosition();
		Position position2 = this.getPosition().shifted(positionOffset, 0);

		movementListener.beforeMovement(this, position1, position2, ApplicationContext.instance());
		movementListener.onLeavingTile(this, position1, position2, ApplicationContext.instance());

		this.setPosition(position2);

		movementListener.onEnteringTile(this, position1, position2, ApplicationContext.instance());
		movementListener.onInteractingWithTile(this, position1, position2, ApplicationContext.instance());
		movementListener.afterMovement(this, position1, position2, ApplicationContext.instance());
	}

	public void shiftVertical(int positionOffset) {
		Position position1 = this.getPosition();
		Position position2 = this.getPosition().shifted(0, positionOffset);

		movementListener.beforeMovement(this, position1, position2, ApplicationContext.instance());
		movementListener.onLeavingTile(this, position1, position2, ApplicationContext.instance());

		this.setPosition(position2);

		movementListener.onEnteringTile(this, position1, position2, ApplicationContext.instance());
		movementListener.onInteractingWithTile(this, position1, position2, ApplicationContext.instance());
		movementListener.afterMovement(this, position1, position2, ApplicationContext.instance());
	}

	public Ray getPortalRay() {
		return portalRay;
	}

	public void activatePortalRay() {
		Ray ray = Ray.create(ApplicationContextUtils.mapOfPlayerLocation(), this);
		this.portalRay = ray;

	}

	public void skipTurn() {
		movementListener.beforeWaiting(this, ApplicationContext.instance());
		movementListener.afterWaiting(this, ApplicationContext.instance());
	}

	public void beginFight() {
		movementListener.beforeFight(this, ApplicationContext.instance());
	}

	public void endFight() {
		movementListener.afterFight(this, ApplicationContext.instance());
	}

	public void beginTeleportation() {
		movementListener.beforeTeleportation(this, ApplicationContext.instance());
	}

	public void beginRespawn() {
		movementListener.beforeRespawn(this, ApplicationContext.instance());
	}

	public void endTeleportation() {
		movementListener.afterTeleportation(this, ApplicationContext.instance());
	}

	public void endRespawn() {
		movementListener.afterRespawn(this, ApplicationContext.instance());
	}

	public void endSwitchingPlane() {
		movementListener.afterSwitchingPlane(this, ApplicationContext.instance());
	}

	public void switchPlane(MapType mapType) {
		this.getPosition().switchPlane(mapType);
		endSwitchingPlane();
	}

	/**
	 * There is also a setDirection in the super class. The difference is that
	 * the latter does not call listeners. It is used for enemies and also when player's initial direction
	 * is set after loading or when starting a new game.
	 */
	public void turn(DirectionType direction) {

		DirectionType before = getDirection();
		DirectionType after = direction;
		movementListener.beforeTurning(before, after);
		setDirection(direction);
		movementListener.afterTurning(before, after);
	}

	public void deactivatePortalRay() {
		portalRay = null;
	}
}
