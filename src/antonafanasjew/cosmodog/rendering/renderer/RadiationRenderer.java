package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.tiledmap.TiledMapLayer;
import antonafanasjew.cosmodog.tiledmap.TiledTile;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class RadiationRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		//Start drawing at the beginning of the scene. Usually, it is the top left corner of the screen.
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());


		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

		Cam cam = cosmodogGame.getCam();

		//The scaled tile width and height.
		//They are the original tile width and height multiplied by the zoom factor of the camera.
		//Example: An 16x16 tile with a zoom factor of 2 will be drawn as a 32x32 tile.
		int scaledTileLength = (int) (tileLength * cam.getZoomFactor());

		//The position of the camera on the map in pixels.
		//Example: Having the cam on the tile 5,5 and the tile width is 16 pixels, the cam position on the map in pixels is 80,80.
		//While the player is moving to tile 5,6, the cam will follow him and its position will gradually change from 80,80 to 80,96.
		int camPositionOnMapInPixelsX = (int) cam.viewCopy().x();
		int camPositionOnMapInPixelsY = (int) cam.viewCopy().y();

		//While the player is moving between tiles, the cam will not be exactly on a tile, but between two tiles.
		//These variables show the offset from the beginning of the tile during the movement.
		int camOffsetFromTileBeginX = (int) ((camPositionOnMapInPixelsX % scaledTileLength));
		int camOffsetFromTileBeginY = (int) ((camPositionOnMapInPixelsY % scaledTileLength));

		//The position of the camera on the map in tiles. It ignores offsets while transitioning the cam from tile to tile.
		int camPositionOnMapInTilesX = camPositionOnMapInPixelsX / scaledTileLength;
		int camPositionOnMapInTilesY = camPositionOnMapInPixelsY / scaledTileLength;

		//Defines how many (scaled) tiles fit in the camera's view. Adds two tiles to have some buffer.
		int camViewWidthInTiles = (int) (cam.viewCopy().width()) / scaledTileLength + 2;
		int camViewHeightInTiles = (int) (cam.viewCopy().height()) / scaledTileLength + 2;

		//When the cam sits between two tiles (for instance when the player moves or teleports)
		//the top row and/or the left column of tiles on the screen will be visible only partially.
		//The offset causes the drawing to start at a negative offset related to the top left corner of the screen.
		graphics.translate(-camOffsetFromTileBeginX, -camOffsetFromTileBeginY);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());


		for (int tilePositionOnMapX = camPositionOnMapInTilesX; tilePositionOnMapX < camPositionOnMapInTilesX + camViewWidthInTiles; tilePositionOnMapX++) {
			for (int tilePositionOnMapY = camPositionOnMapInTilesY; tilePositionOnMapY < camPositionOnMapInTilesY + camViewHeightInTiles; tilePositionOnMapY++) {

				if (tilePositionOnMapX < 0) {
					continue;
				}

				if (tilePositionOnMapY < 0) {
					continue;
				}

				if (tilePositionOnMapX >= map.getMapType().getWidth()) {
					continue;
				}

				if (tilePositionOnMapY >= map.getMapType().getHeight()) {
					continue;
				}
				Position position = Position.fromCoordinates(tilePositionOnMapX, tilePositionOnMapY, map.getMapType());
				render(map, (tilePositionOnMapX - camPositionOnMapInTilesX) * tileLength, (tilePositionOnMapY - camPositionOnMapInTilesY) * tileLength, position);
			}
		}


		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(camOffsetFromTileBeginX, camOffsetFromTileBeginY);


		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());

	}

	private void render(CosmodogMap map, int offsetX, int offsetY, Position tilePosition) {

		TiledMapLayer radiationLayer = map.getCustomTiledMap().getMapLayersByNames().get("Meta_radiation");
		TiledTile tile = radiationLayer.getTile(tilePosition);

		if (tile == null) {
			return;
		}
		if (tile.getGid() != TileType.RADIATION.getTileId() && tile.getGid() != TileType.RADIATION_SAFE_ZONE.getTileId()) {
			return;
		}

		Animation animation = ApplicationContext.instance().getAnimations().get("radiation");
		animation.draw(offsetX, offsetY, animation.getWidth(), animation.getHeight());

	}

}
