package antonafanasjew.cosmodog.actions;

/**
 * Domain values of the action types set.
 */
public enum AsyncActionType {
	
	WEAPON_TOOLTIP(false, true),
	
	/**
	 * Action type: Overhead notification. Indicates an overhead notification for player and NPC. F.i. score or hit points.
	 */
	OVERHEAD_NOTIFICATION(false, true),
	
	/**
	 * Action type: Teleportation.
	 */
	TELEPORTATION(true, true),
	
	/**
	 * Action type: Movement of the player character and the NPC's
	 */
	MOVEMENT(true, false),
	
	/**
	 * Action type: Wait. Is used to make intervals between movements of the player.
	 */
	WAIT(true, false),
	
	WORM_ATTACK(true, false),
	
	/**
	 * Action type: Collision indicator. Is used to make intervals between the 'no passage' sounds. Does not block the input.
	 */
	COLLISION_INDICATOR(false, false),
	
	/**
	 * Modal action to represent a movement attempt when the passage is blocked.
	 */
	MOVEMENT_ATTEMPT(true, true),
	
	/**
	 * Action type: Fight. Indicates a fight sequence with multiple enemies. This action will have it's own action registry
	 * and register phases as asynchronous actions.
	 */
	FIGHT(true, true),
	
	/**
	 * Action type: Blocking interface. Is used for all interface actions that block the normal control flow.
	 * F.i. Modal windows.
	 */
	BLOCKING_INTERFACE(true, true),
	
	/**
	 * Action type: Non blocking interface. Used for all interface actions that do not block input.
	 * F.i. Alisa's comments.
	 */
	NON_BLOCKING_INTERFACE(false, false);
	
	private boolean blocksInput;
	private boolean stackable;
	
	private AsyncActionType(boolean blocksInput, boolean stackable) {
		this.blocksInput = blocksInput;
		this.stackable = stackable;
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
	
}
