package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.SpriteSheets;
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
					if (tx >= 0 && ty >= 0) {
						if (rendererPredicate.tileShouldBeRendered(i, tx, ty)) {
	    					if (i == Layers.LAYER_WATER && cosmodogGame.getChronometer().timeFrameInLoop(2, 500) == 0) {
	    						render(tiledMap, (tx - tileNoX) * tileWidth, (ty - tileNoY) * tileHeight, tx, ty, Layers.LAYER_META_ALT_WATER);
	    					} else {
	    						render(tiledMap, (tx - tileNoX) * tileWidth, (ty - tileNoY) * tileHeight, tx, ty, i);
	    					}
						}
					}
				}
			}
			
		}

		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
	}

	private void render(CustomTiledMap customTiledMap, int offsetX, int offsetY, int tilePosX, int tilePosY, int layerIndex) {
		
		int tileId = customTiledMap.getTileId(tilePosX, tilePosY, layerIndex);
		
		int imageIndex = tileId - 1;
		
		if (imageIndex >= 0) {
		
			int imagePosX = imageIndex % 9;
			int imagePosY = imageIndex / 9;
			
			SpriteSheet tilesetSpriteSheet = ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_TILES);
			Image tileImage = tilesetSpriteSheet.getSprite(imagePosX, imagePosY);
			
			tileImage.draw(offsetX, offsetY, tileImage.getWidth(), tileImage.getHeight());
		}
	}
	
}
