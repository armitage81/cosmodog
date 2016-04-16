package antonafanasjew.cosmodog.rendering.renderer.pieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.Piece;

public abstract class AbstractPieceRenderer implements PieceRenderer {

	@Override
	public void renderPiece(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		render(applicationContext, tileWidth, tileHeight, tileNoX, tileNoY, piece);
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
		
		CollectibleGoodie goodie = (CollectibleGoodie)piece;
		
		String goodieTypeRepr = goodie.getGoodieType().name(); 
		
		applicationContext.getAnimations().get(goodieTypeRepr).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);
	}
	
	protected abstract void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece);

}
