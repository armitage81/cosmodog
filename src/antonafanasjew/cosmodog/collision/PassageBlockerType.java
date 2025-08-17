package antonafanasjew.cosmodog.collision;

/**
 * Describes the reason for a passage blocker (previously reason for collision)
 *
 */
public enum PassageBlockerType {

	/**
	 * Pseudo blocker that is actually a way to describe a non blocked passage.
	 */
	PASSABLE("Not blocked"),
	
	/**
	 * The tile is not passable because it is blocked by obstacles.
	 */
	BLOCKED(""),
	
	BLOCKED_ON_WHEELS("Blocked. [SHIFT] + [Arrow Key] to exit"),

	BLOCKED_ON_WHEELS_DURING_RACE("Stay on track!"),
	
	BLOCKED_BY_INACTIVE_PIECE("Blocked"),
	
	/**
	 * The tile is not passable because it is blocked as target by an other character.
	 */
	BLOCKED_AS_TARGET_BY_OTHER_MOVING_CHARACTER("Blocked by other character."),
	
	/**
	 * The tile is not passable because the vehicle has no fuel.
	 */
	FUEL_EMPTY("No fuel. [SHIFT] + [Arrow Key] to exit"),

	/**
	 * The tile is out of the home region of an npc.
	 */
	OUT_OF_HOME_REGION("Out of home region"),

	/**
	 * The tile is in a safa space, moveable group, portal puzzle, race or other puzzle.
	 */
	PUZZLE_REGION("Puzzle region"),
	
	/**
	 * The tile contains an energy wall generator and the costs are higher than the number of collected infobits.
	 */
	ENERGY_WALL_COSTS("Requires %s infobits"),
	
	/**
	 * The tile is blocked by a vehicle collectible. F.i. another vehicle or enemies cannot pass such tiles.
	 */
	BLOCKED_BY_VEHICLE_COLLECTIBLE("Blocked by vehicle"),
	
	/**
	 * Passage blocked as the target tile has no rails. (Used for the big platform)
	 */
	BLOCKED_NO_RAILS("No rails. [SHIFT] + [Arrow Key] to exit"),
	
	/**
	 * Blocked by (mostly destroyable) obstacle set on the map as dynamic piece.
	 */
	BLOCKED_DYNAMIC_PIECE("%s");
	
	private String descriptionTemplate;
	
	private PassageBlockerType(String descriptionTemplate) {
		this.descriptionTemplate = descriptionTemplate;
	}
	
	public String getDescriptionTemplate() {
		return descriptionTemplate;
	}
	
}
