package antonafanasjew.cosmodog.domains;

/**
 * Domain for player appearance types. Usually used to render the player character in an appropriate way. 
 */
public enum PlayerAppearanceType {
	
	/**
	 * Normal appearance as a player character sprite.
	 */
	DEFAULT,
	
	/**
	 * Visible as vehicle.
	 */
	INVEHICLE,
	
	/**
	 * Player character on boat sprite.
	 */
	ONBOAT, 
	
	/**
	 * Covered partially by high grass.
	 */
	INHIGHGRASS
}
