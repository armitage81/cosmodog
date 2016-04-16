package antonafanasjew.cosmodog.rendering.renderer.pieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.Piece;

public class ToolRenderer extends AbstractPieceRenderer {

	@Override
	protected void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		applicationContext.getAnimations().get("collectibleItemTool").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);		
	}

}
