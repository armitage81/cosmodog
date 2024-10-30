package antonafanasjew.cosmodog.model;


/**
 * Describes dynamic pieces, that is tiles that can change their properties.
 * F.i. an destroyable stone.
 */
public abstract class DynamicPiece extends Piece {

	private static final long serialVersionUID = -4951335050582899992L;
	
	/**
	 * Executed when the player tries to enter the tile with this piece.
	 * This method will only be called if the dynamic piece is blocking, f.i. when the player hits on the hard stone.
	 * Actual movement (f.i. when stepping on a plate or poison) will not call this method. Instead, the method interactWhenSteppingOn() will be called.
	 */
	public void interact() {
		
	};
	
	/**
	 * This method will be called only when stepping on a dynamic piece, such as a pressure plate or poison pool.
	 * If the dynamic piece is blocking, then the interact() method is called instead.
	 */
	public void interactWhenSteppingOn() {
		
	}
	
	/**
	 * This method will be called only when leacing on a dynamic piece, such as a pressure plate or poison pool.
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

	public abstract String animationId(boolean bottomNotTop);
	
}
