package antonafanasjew.cosmodog.model.actors;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.topology.Position;


public class Vehicle extends Transport {

	private static final long serialVersionUID = -2970030463088280229L;

	public static final int MAX_POSSIBLE_LIFE = 100;
	public static final int INITIAL_LIFE = 10;
	
	public static final int INITIAL_MAX_FUEL = 100;
    public static final int HIGHEST_MAX_FUEL = 400;

	private int maxFuel = INITIAL_MAX_FUEL;

	private int fuel = maxFuel;
	
	public Vehicle() {
		this.setMaxLife(INITIAL_LIFE);
		this.setLife(INITIAL_LIFE);
		this.setDirection(DirectionType.DOWN);
	}
	
	public static Vehicle fromPosition(Position position) {
		Vehicle vehicle = new Vehicle();
		vehicle.setPosition(position);
		return vehicle;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int n) {
		this.fuel = n;
		if (this.fuel < 0) {
			this.fuel = 0;
		}
	}
	
	public void decreaseFuel(int n) {
		this.setFuel(this.getFuel() - n);
	}
	
	public boolean outOfFuel() {
		return fuel <= 0;
	}

	/**
	 * Each upgrade increases the maximal fuel of the vehicle by the half of the initial maximal fuel.
	 * <p>
	 * Example: Initial maximal fuel is 50 and there were already 2 upgrades. Current maximal fuel is then 100.
	 * The new maximal fuel after the upgrade will be 125.
	 */
	public void upgradeMaxFuel() {
		int increment = INITIAL_MAX_FUEL / 2;
		int currentIncrements = maxFuel / increment;
        this.maxFuel = (currentIncrements + 1) * increment;
	}

	public int getMaxFuel() {
		return maxFuel;
	}
}
