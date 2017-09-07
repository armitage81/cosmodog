package antonafanasjew.cosmodog.domains;

/**
 * Domain for unit types.
 *
 */
public enum UnitType {
	
	/**
	 * Light tank.
	 */
	LIGHT_TANK(false, true, false),
	
	/**
	 * Humanoid robot.
	 */
	ROBOT(false, true, false),
	
	/**
	 * Flying drone.
	 */
	DRONE(false, true, false),
	
	/**
	 * Immovable turret
	 */
	TURRET(false, true, false),
	
	/**
	 * Pig rat from the swamps.
	 */
	PIGRAT(true, false, false),
	
	/**
	 * Wheeled artillery.
	 */
	ARTILLERY(false, true, true),
	
	/**
	 * Trike.
	 */
	SCOUT(false, true, false),
	
	/**
	 * Heavy armored floating drone in the alien base
	 */
	FLOATER(false, true, false),
	
	CONDUCTOR(false, true, true),
	
	GUARDIAN(false, false, true),
	
	;
	
	private boolean alive;
	private boolean robotic;
	private boolean rangedUnit;
	
	private UnitType(boolean alive, boolean robotic, boolean rangedUnit) {
		this.alive = alive;
		this.robotic = robotic;
		this.rangedUnit = rangedUnit;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isRobotic() {
		return robotic;
	}

	public boolean isRangedUnit() {
		return rangedUnit;
	}

	public void setRangedUnit(boolean rangedUnit) {
		this.rangedUnit = rangedUnit;
	}
}
