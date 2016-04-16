package antonafanasjew.cosmodog.rendering.renderer.pieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.Piece;

public class SoulEssenceRenderer extends AbstractPieceRenderer {

	@Override
	protected void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		renderAsDefaultFeatureBoundGoodie(applicationContext, tileWidth, tileHeight, tileNoX, tileNoY, piece, Features.FEATURE_SOULESSENCE);
	}

}
