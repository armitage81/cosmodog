package antonafanasjew.cosmodog.domains;

/**
 * Action types for NPC. They will be used to define what an NPC is doing at a given time (f.i. to render a proper animation.)
 */
public enum NpcActionType {

	/**
	 * Actor is idle.
	 */
	INANIMATE,
	
	/**
	 * Actor is moving.
	 */
	ANIMATE,
	
	/**
	 * Actor is attacking.
	 */
	SHOOTING,
	
	/**
	 * Actor is being destroyed.
	 */
	EXPLODING
}
