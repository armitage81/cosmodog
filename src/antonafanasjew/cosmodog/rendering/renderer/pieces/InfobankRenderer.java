package antonafanasjew.cosmodog.rendering.renderer.pieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.Piece;

public class InfobankRenderer extends AbstractPieceRenderer {

	@Override
	protected void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		renderAsDefaultGoodie(applicationContext, tileWidth, tileHeight, tileNoX, tileNoY, piece);
	}


}
