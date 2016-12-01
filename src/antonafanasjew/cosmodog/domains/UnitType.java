package antonafanasjew.cosmodog.domains;

/**
 * Domain for unit types.
 *
 */
public enum UnitType {
	
	/**
	 * Light tank.
	 */
	LIGHT_TANK(false, true),
	
	/**
	 * Humanoid robot.
	 */
	ROBOT(false, true),
	
	/**
	 * Flying drone.
	 */
	DRONE(false, true),
	
	/**
	 * Immovable turret
	 */
	TURRET(false, true),
	
	/**
	 * Pig rat from the swamps.
	 */
	PIGRAT(true, false);
	
	private boolean alive;
	private boolean robotic;
	
	private UnitType(boolean alive, boolean robotic) {
		this.alive = alive;
		this.robotic = robotic;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isRobotic() {
		return robotic;
	}
}
