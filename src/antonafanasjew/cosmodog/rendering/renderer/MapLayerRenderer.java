package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.topology.Position;
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

		//Start drawing at the beginning of the scene. Usually, it is the top left corner of the screen.
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		MapLayerRendererPredicate rendererPredicate = (MapLayerRendererPredicate)renderingParameter;
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		Cam cam = cosmodogGame.getCam();

		//The original tile width and height. They are usually 16x16 pixels big.
		int tileWidthInPixels = map.getTileWidth();
		int tileHeightInPixels = map.getTileHeight();

		//The scaled tile width and height.
		//They are the original tile width and height multiplied by the zoom factor of the camera.
		//Example: An 16x16 tile with a zoom factor of 2 will be drawn as a 32x32 tile.
		int scaledTileWidthInPixels = (int) (tileWidthInPixels * cam.getZoomFactor());
		int scaledTileHeightInPixels = (int) (tileHeightInPixels * cam.getZoomFactor());

		//The position of the camera on the map in pixels.
		//Example: Having the cam on the tile 5,5 and the tile width is 16 pixels, the cam position on the map in pixels is 80,80.
		//While the player is moving to tile 5,6, the cam will follow him and its position will gradually change from 80,80 to 80,96.
		int camPositionOnMapInPixelsX = (int) cam.viewCopy().x();
		int camPositionOnMapInPixelsY = (int) cam.viewCopy().y();

		//While the player is moving between tiles, the cam will not be exactly on a tile, but between two tiles.
		//These variables show the offset from the beginning of the tile during the movement.
		int camOffsetFromTileBeginX = (int) ((camPositionOnMapInPixelsX % scaledTileWidthInPixels));
		int camOffsetFromTileBeginY = (int) ((camPositionOnMapInPixelsY % scaledTileHeightInPixels));

		//The position of the camera on the map in tiles. It ignores offsets while transitioning the cam from tile to tile.
		int camPositionOnMapInTilesX = camPositionOnMapInPixelsX / scaledTileWidthInPixels;
		int camPositionOnMapInTilesY = camPositionOnMapInPixelsY / scaledTileHeightInPixels;

		//Defines how many (scaled) tiles fit in the camera's view. Adds two tiles to have some buffer.
		int camViewWidthInTiles = (int) (cam.viewCopy().width()) / scaledTileWidthInPixels + 2;
		int camViewHeightInTiles = (int) (cam.viewCopy().height()) / scaledTileHeightInPixels + 2;

		//When the cam sits between two tiles (for instance when the player moves or teleports)
		//the top row and/or the left column of tiles on the screen will be visible only partially.
		//The offset causes the drawing to start at a negative offset related to the top left corner of the screen.
		graphics.translate(-camOffsetFromTileBeginX, -camOffsetFromTileBeginY);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

			
		for (int i = 0; i < Layers.LAYER_META_COLLISIONS; i++) {
			//We paint each tile individually as 1*1 tile map section to be flexible on skipping and replacing tiles.
			for (int tilePositionOnMapX = camPositionOnMapInTilesX; tilePositionOnMapX < camPositionOnMapInTilesX + camViewWidthInTiles; tilePositionOnMapX++) {
				for (int tilePositionOnMapY = camPositionOnMapInTilesY; tilePositionOnMapY < camPositionOnMapInTilesY + camViewHeightInTiles; tilePositionOnMapY++) {

					Position tilePosition = Position.fromCoordinates(tilePositionOnMapX, tilePositionOnMapY);
					if (!tilePosition.inMapBounds(map)) {
						continue;
					}

					if (!rendererPredicate.tileShouldBeRendered(i, tilePosition)) {
						continue;
					}

					render(map, (tilePositionOnMapX - camPositionOnMapInTilesX) * tileWidthInPixels, (tilePositionOnMapY - camPositionOnMapInTilesY) * tileHeightInPixels, tilePosition, i);
				}
			}
			
		}

		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(camOffsetFromTileBeginX, camOffsetFromTileBeginY);
		
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
		
	}

	private void render(CosmodogMap map, int offsetX, int offsetY, Position tilePosition, int layerIndex) {
		
		Animation animation = null;
		
		int tileId = map.getTileId(tilePosition, layerIndex);
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
