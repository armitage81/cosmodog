package antonafanasjew.cosmodog.actions;

/**
 * Domain values of the action types set.
 */
public enum AsyncActionType {
	
	MOVEMENT(true, false),
	WAIT(true, false),
	COLLISION_INDICATOR(false, false),
	FIGHT(true, true),
	
	BLOCKING_INTERFACE(true, true),
	NON_BLOCKING_INTERFACE(false, false);
	
	private boolean blocksInput;
	private boolean stackable;
	
	private AsyncActionType(boolean blocksInput, boolean stackable) {
		this.blocksInput = blocksInput;
		this.stackable = stackable;
	}

	public boolean isBlocksInput() {
		return blocksInput;
	}
	
	public boolean isStackable() {
		return stackable;
	}
	
}
