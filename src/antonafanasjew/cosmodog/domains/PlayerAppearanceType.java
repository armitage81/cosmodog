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
	INHIGHGRASS, 
	
	/**
	 * Invisible because of teleportation.
	 */
	ISTELEPORTING,
	
	/**
	 * sliding on ski.
	 */
	ONSKI,
	
	/**
	 * No feet because sunk in rough terrain, e.g. snow, sand, small grass
	 */
	NOFEET
}
