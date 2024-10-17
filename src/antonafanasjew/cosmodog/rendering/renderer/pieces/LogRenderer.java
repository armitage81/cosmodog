package antonafanasjew.cosmodog.rendering.renderer.pieces;

import org.newdawn.slick.Animation;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.Piece;

public class LogRenderer extends AbstractPieceRenderer {

	@Override
	protected void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		
		String animationId = "collectibleItemLogCard";
		
		Animation animation = applicationContext.getAnimations().get(animationId);
		
		animation.draw((piece.getPosition().getX() - tileNoX) * tileWidth, (piece.getPosition().getY() - tileNoY) * tileHeight);
		
	}

}
