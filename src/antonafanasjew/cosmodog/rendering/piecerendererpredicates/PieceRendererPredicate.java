package antonafanasjew.cosmodog.rendering.piecerendererpredicates;

import antonafanasjew.cosmodog.model.Piece;

public interface PieceRendererPredicate {

	boolean pieceShouldBeRendered(Piece piece);
	
}
