package antonafanasjew.cosmodog.rendering.renderer.pieces;

import org.newdawn.slick.Animation;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.particlepattern.model.GridParticlePatternBuilder;

public abstract class AbstractPieceRenderer implements PieceRenderer {

	@Override
	public void renderPiece(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		
		// Don't render the collectible if it is wrapped by a dynamic piece, such as a crate.
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		DynamicPiece dynamicPiece = map.dynamicPieceAtPosition(piece.getPositionX(), piece.getPositionY());
		
		if (dynamicPiece == null || dynamicPiece.wrapsCollectible() == false) {
			render(applicationContext, tileWidth, tileHeight, tileNoX, tileNoY, piece);
		}
	}

	protected void renderAsDefaultFeatureBoundGoodie(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece, String feature) {
		Features.getInstance().featureBoundProcedure(feature, new Runnable() {
			@Override
			public void run() {
				renderAsDefaultGoodie(applicationContext, tileWidth, tileHeight, tileNoX, tileNoY, piece);
			}
		});
	}

	protected void renderAsDefaultGoodie(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {

		CollectibleGoodie goodie = (CollectibleGoodie) piece;
		String goodieTypeRepr = goodie.getGoodieType().name();
		Animation animation = applicationContext.getAnimations().get(goodieTypeRepr);
		animation.draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);

	}

	protected abstract void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece);

}
