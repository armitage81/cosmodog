package antonafanasjew.cosmodog.collision;

/**
 * Describes the reason for a passage blocker (previously reason for collision)
 *
 */
public enum PassageBlocker {

	/**
	 * Pseudo blocker that is actually a way to describe a non blocked passage.
	 */
	PASSABLE,
	
	/**
	 * The tile is not passable because it is blocked by obstacles.
	 */
	BLOCKED,
	
	/**
	 * The tile is not passable because it is blocked as target by an other character.
	 */
	BLOCKED_AS_TARGET_BY_OTHER_MOVING_CHARACTER,
	
	/**
	 * The tile is not passable because the vehicle has no fuel.
	 */
	FUEL_EMPTY,

	/**
	 * The tile is out of the home region of an npc.
	 */
	OUT_OF_HOME_REGION,
	
	/**
	 * The tile contains an energy wall generator and the costs are higher than the number of collected infobits.
	 */
	ENERGY_WALL_COSTS,
	
	/**
	 * The tile is blocked by a vehicle collectible. F.i. another vehicle or enemies cannot pass such tiles.
	 */
	BLOCKED_BY_VEHICLE_COLLECTIBLE,
	
}
