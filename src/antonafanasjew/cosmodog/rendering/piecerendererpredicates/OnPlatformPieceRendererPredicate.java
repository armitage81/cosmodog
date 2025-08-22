package antonafanasjew.cosmodog.rendering.piecerendererpredicates;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

/**
 * This predicate returns true, if the piece is on the platform, whether occupied or standalone.
 */
public class OnPlatformPieceRendererPredicate implements PieceRendererPredicate {

	@Override
	public boolean pieceShouldBeRendered(Piece piece) {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
        return (!(piece instanceof Platform)) && map.getMapPieces().allPositionsCoveredByPlatforms().contains(piece.getPosition());
	}

}
