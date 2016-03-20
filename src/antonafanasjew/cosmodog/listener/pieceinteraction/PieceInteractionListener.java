package antonafanasjew.cosmodog.listener.pieceinteraction;

import java.io.Serializable;

import antonafanasjew.cosmodog.model.Piece;

/**
 * This listener describes reactions on piece interactions (f. i. "collected infobit", "found car" etc).
 */
public interface PieceInteractionListener extends Serializable {

	/**
	 * Will be called before interaction with the piece.
	 */
	void beforeInteraction(Piece piece);
	
	/**
	 * Will be called after interaction with the piece.
	 */
	void afterInteraction(Piece piece);
	
}
