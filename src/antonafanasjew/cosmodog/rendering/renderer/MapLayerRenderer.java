package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.MapLayerRendererPredicate;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.Mappings;
import antonafanasjew.cosmodog.util.TilesetUtils;



public class MapLayerRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		MapLayerRendererPredicate rendererPredicate = (MapLayerRendererPredicate)renderingParameter;
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		Cam cam = cosmodogGame.getCam();
		
		
		int tileWidth = map.getTileWidth();
		int tileHeight = map.getTileHeight();

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
					if (tx >= 0 && ty >= 0 && tx < map.getWidth() && ty < map.getHeight()) {
						if (rendererPredicate.tileShouldBeRendered(i, tx, ty)) {
							render(map, (tx - tileNoX) * tileWidth, (ty - tileNoY) * tileHeight, tx, ty, i);
						}
					}
				}
			}
			
		}

		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
		
	}

	private void render(CosmodogMap map, int offsetX, int offsetY, int tilePosX, int tilePosY, int layerIndex) {
		
		Animation animation = null;
		
		int tileId = map.getTileId(tilePosX, tilePosY, layerIndex);
		TileType tileType = TileType.getByLayerAndTileId(layerIndex, tileId);
		if (Mappings.TILE_TYPES_TO_BE_ANIMATED.contains(tileType)) {
			animation = ApplicationContext.instance().getAnimations().get("tile." + tileType.name());
		}
		
		if (animation != null) {
			animation.draw(offsetX, offsetY, animation.getWidth(), animation.getHeight());
		} else {
			int imageIndex = tileId - 1;
			
			if (imageIndex >= 0) {
			
				int imagePosX = imageIndex % 9;
				int imagePosY = imageIndex / 9;
				
				Image tileImage = TilesetUtils.tileByIndex(imagePosX, imagePosY);
				
				tileImage.draw(offsetX, offsetY, tileImage.getWidth(), tileImage.getHeight());
			}	
		}
		
	}
	
}
