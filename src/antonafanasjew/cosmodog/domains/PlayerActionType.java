package antonafanasjew.cosmodog.domains;

/**
 * Domain for player character actions. Will be used to define what PC is doing at any given time (f.i. to render the proper animation).
 */
public enum PlayerActionType {
	
	/**
	 * Player character is idle.
	 */
	INANIMATE,
	
	/**
	 * Player character is moving.
	 */
	ANIMATE,
	
	/**
	 * Player character is taking damage during a fight phase.
	 */
	TAKINGDAMAGE,
	
	HOLDING_UP_ITEM,
}