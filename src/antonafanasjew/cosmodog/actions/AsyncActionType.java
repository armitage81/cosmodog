package antonafanasjew.cosmodog.actions;

/**
 * Domain values of the action types set.
 */
public enum AsyncActionType {

	FOCUSING_ON_PLAYER_AFTER_USING_PORTAL(true, false, true),

	SPACE_LIFT(true, false, true),

	SNOWFALL_CHANGE(false, false, false),

	FOUND_TOOL_JINGLE(false, false, false),

	LOST_JINGLE(false, false, true),
	
	/**
	 * Player is dying
	 */
	DYING(true, true, true),
	
	CUTSCENE(true, true, true),
	
	WEAPON_TOOLTIP(false, true, false),
	
	/**
	 * Action type: Overhead notification. Indicates an overhead notification for player and NPC. F.i. score or hit points.
	 */
	OVERHEAD_NOTIFICATION(false, true, false),
	
	/**
	 * Action type: Onscreen notification. It is a text that is smacked on screen without blocking the input.
	 */
	ONSCREEN_NOTIFICATION(false, true, false),
	
	/**
	 * Action type: Movement of the player character and the NPC's
	 */
	MOVEMENT(true, true, true),
	
	/**
	 * Action type: Wait. Is used to make intervals between movements of the player.
	 */
	WAIT(true, false, true),
	
	WORM_ATTACK(true, false, true),
	
	MINE_EXPLOSION(true, false, true),

	POISON_DAMAGE(true, false, true),
	
	RADIATION_DAMAGE(true, false, true),
	SHOCK_DAMAGE(true, false, true),
	
	DEATH_BY_SHOCK(true, false, true),
	DEATH_BY_RADIATION(true, false, true),
	
	/**
	 * Action type: Collision indicator. Is used to make intervals between the 'no passage' sounds. Does not block the input.
	 */
	COLLISION_INDICATOR(false, false, true),
	
	/**
	 * Modal action to represent a movement attempt when the passage is blocked.
	 */
	MOVEMENT_ATTEMPT(true, true, true),
	
	/**
	 * Action type: Fight. Indicates a fight sequence with multiple enemies. This action will have it's own action registry
	 * and register phases as asynchronous actions.
	 */
	FIGHT(true, true, true),
	
	/**
	 * Fight from platform. This is a simple fight where all enemies targeted by the platform are killed without a chance to retaliate.
	 */
	FIGHT_FROM_PLATFORM(true, true, true),
	
	MODAL_WINDOW(true, true, false),

	PRESENTING_NEW_TOOL(true, false, false),

	RESPAWNING(true, false, true);
	
	private boolean blocksInput;
	private boolean stackable;
	private boolean blockableByInterface;
	
	private AsyncActionType(boolean blocksInput, boolean stackable, boolean blockableByInterface) {
		this.blocksInput = blocksInput;
		this.stackable = stackable;
		this.blockableByInterface = blockableByInterface;
	}

	/**
	 * Returns the flag whether the input is blocked by this action or not.
	 * @return Input blocked flag.
	 */
	public boolean isBlocksInput() {
		return blocksInput;
	}
	
	/**
	 * Returns the flag whether this action type is stackable or not. Stackable actions will be queued up
	 * in the action registry.
	 * @return Stackable flag.
	 */
	public boolean isStackable() {
		return stackable;
	}

	public boolean isBlockableByInterface() {
		return blockableByInterface;
	}
}


