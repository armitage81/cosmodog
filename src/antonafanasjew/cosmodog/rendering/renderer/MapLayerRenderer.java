package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.MapLayerRendererPredicate;



public class MapLayerRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		MapLayerRendererPredicate rendererPredicate = (MapLayerRendererPredicate)renderingParameter;
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();
		Cam cam = cosmodogGame.getCam();
		
		
		int tileWidth = tiledMap.getTileWidth();
		int tileHeight = tiledMap.getTileHeight();

		int scaledTileWidth = (int) (tileWidth * cam.getZoomFactor());
		int scaledTileHeight = (int) (tileHeight * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileWidth));
		int y = -(int) ((camY % scaledTileHeight));

		int tileNoX = camX / scaledTileWidth;
		int tileNoY = camY / scaledTileHeight;

		int tilesW = (int) (cam.viewCopy().width()) / scaledTileWidth + 2;
		int tilesH = (int) (cam.viewCopy().height()) / scaledTileHeight + 2;
		
		graphics.translate(x, y);		
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

			
		for (int i = 0; i < Layers.LAYER_META_COLLISIONS; i++) {
			//We paint each tile individually as 1*1 tile map section to be flexible on skipping and replacing tiles.
			for (int tx = tileNoX; tx < tileNoX + tilesW; tx++) {
				for (int ty = tileNoY; ty < tileNoY + tilesH; ty++) {
					if (rendererPredicate.tileShouldBeRendered(i, tx, ty)) {
    					if (i == Layers.LAYER_WATER && cosmodogGame.getChronometer().timeFrameInLoop(2, 500) == 0) {
    						tiledMap.render((tx - tileNoX) * tileWidth, (ty - tileNoY) * tileHeight, tx, ty, 1, 1, Layers.LAYER_META_ALT_WATER, false);
    					} else {
    						tiledMap.render((tx - tileNoX) * tileWidth, (ty - tileNoY) * tileHeight, tx, ty, 1, 1, i, false);
    					}
					}
				}
			}
			
		}

		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
	}

}
