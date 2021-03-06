package antonafanasjew.cosmodog.model.actors;

import antonafanasjew.cosmodog.domains.DirectionType;


public class Vehicle extends Transport {

	private static final long serialVersionUID = -2970030463088280229L;

	public static final int MAX_POSSIBLE_LIFE = 100;
	public static final int INITIAL_LIFE = 10;
	
	public static final int MAX_FUEL = 200;
	
	private int fuel = MAX_FUEL;
	
	public Vehicle() {
		this.setMaxLife(INITIAL_LIFE);
		this.setLife(INITIAL_LIFE);
		this.setDirection(DirectionType.DOWN);
	}
	
	public static Vehicle fromPosition(int positionX, int positionY) {
		Vehicle vehicle = new Vehicle();
		vehicle.setPositionX(positionX);
		vehicle.setPositionY(positionY);
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
		
}
