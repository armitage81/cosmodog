package antonafanasjew.cosmodog.rendering.renderer.piecerendererpredicates;

import antonafanasjew.cosmodog.model.Piece;

public interface PieceRendererPredicate {

	boolean pieceShouldBeRendered(Piece piece);
	
}
