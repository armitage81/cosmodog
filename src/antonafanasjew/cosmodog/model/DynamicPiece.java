package antonafanasjew.cosmodog.model;


import antonafanasjew.cosmodog.domains.DirectionType;

/**
 * Describes dynamic pieces, that is tiles that can change their properties.
 * F.i. an destroyable stone.
 */
public abstract class DynamicPiece extends Piece {

	private static final long serialVersionUID = -4951335050582899992L;

	//There can be multiple dynamic pieces on one position, for instance, a cube on a sensor.
	// In this case random rendering order is not enough since the sensor could be rendered above the cube.
	//Instead, the renderer should render dynamic pieces in order of their priority (first 1, then 2 and last 10.)
	public int renderingPriority() {
		return 10;
	}

	/**
	 * Executed when the player tries to enter the tile with this piece.
	 * This method will only be called if the dynamic piece is blocking, f.i. when the player hits on the hard stone.
	 * Actual movement (f.i. when stepping on a plate or poison) will not call this method. Instead, the method interactWhenSteppingOn() will be called.
	 */
	public void interact() {
		
	};

	/**
	 * This method will be called before the movement. When the player wants to move to a tile with this dynamic piece,
	 * he will first interact with the dynamic piece, then attempting to move to the tile.
	 * Example: Doors that are opened automatically when the player enters the tile. They need to be open before the player moves to the tile.
	 * It could be that some logic would decide whether the door should open or not. This logic could be placed here.
	 * What is the difference to the interact method? The difference is that this method here is called before collision validation.
	 * That means that the player will execute a movement attempt after the interaction.
	 *
	 * Consider two examples:
	 *
	 * 1. "Crumbled wall" piece: If the player has dynamite, he will destroy the wall in the "interact" method, but he will not move into the tile in the same turn.
	 * 2. "Bollard with movement detector": If the player wants to move to the tile, the bollard will be sunk in the interactBeforeEnteringAttempt and the player will enter the tile afterwards.
	 *
	 */
	public void interactBeforeEnteringAttempt() {

	}

	/**
	 * Analog to interactBeforeEnteringAttempt, this method is used to execute an action after the player has left the tile and finished his movement.
	 */
	public void interactAfterExiting() {

	}

	/**
	 * This method will be called only when stepping on a dynamic piece, such as a pressure plate or poison pool.
	 * If the dynamic piece is blocking, then the interact() method is called instead.
	 */
	public void interactWhenSteppingOn() {
		
	}
	
	/**
	 * This method will be called only when leaving on a dynamic piece, such as a pressure plate or poison pool.
	 */
	public void interactWhenLeaving() {
		
	}
	
	/**
	 * Executed when the player approaches the piece.
	 * This method is rarely needed so it provides an own empty implementation
	 * to not have to implement it in every sub class.
	 */
	public void interactWhenApproaching() {
		
	}
	
	/**
	 * Implementations should return the information whether the
	 * dynamic piece is wrapping (making invisible) a potential collectible at the same tile.
	 * Example: A crate is hiding the collectible as long as it is not in the state 'destroyed'.
	 */
	public abstract boolean wrapsCollectible();

	public abstract boolean permeableForPortalRay(DirectionType incomingDirection);

	public abstract String animationId(boolean bottomNotTop);
	
}
