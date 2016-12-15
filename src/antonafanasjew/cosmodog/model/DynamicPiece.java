package antonafanasjew.cosmodog.model;


/**
 * Describes dynamic pieces, that is tiles that can change their properties.
 * F.i. an destroyable stone.
 */
public abstract class DynamicPiece extends Piece {

	private static final long serialVersionUID = -4951335050582899992L;
	
	/**
	 * Executed when the player tries to enter the tile with this piece.
	 */
	public abstract void interact();
	
	/**
	 * Implementations should return the information whether the
	 * dynamic piece is wrapping (making invisible) a potential collectible at the same tile.
	 * Example: A crate is hiding the collectible as long as it is not in the state 'destroyed'.
	 */
	public abstract boolean wrapsCollectible();

	
	
}
