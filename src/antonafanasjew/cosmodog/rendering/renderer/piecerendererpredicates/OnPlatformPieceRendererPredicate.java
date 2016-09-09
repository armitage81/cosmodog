package antonafanasjew.cosmodog.rendering.renderer.piecerendererpredicates;

import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

/**
 * This predicate returns true, if the piece is on the platform, whether occupied or standalone.
 */
public class OnPlatformPieceRendererPredicate implements PieceRendererPredicate {

	@Override
	public boolean pieceShouldBeRendered(Piece piece) {
		boolean retVal = (piece instanceof Platform == false) && CosmodogMapUtils.isTileOnPlatform(piece.getPositionX(), piece.getPositionY());
		return retVal;
	}

}
