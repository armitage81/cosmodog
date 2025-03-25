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
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class RadiationRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());


		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

		Cam cam = cosmodogGame.getCam();

		Cam.CamTilePosition camTilePosition = cam.camTilePosition();

		graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());


		for (int tilePositionOnMapX = camTilePosition.tileX(); tilePositionOnMapX < camTilePosition.tileX() + camTilePosition.widthInTiles(); tilePositionOnMapX++) {
			for (int tilePositionOnMapY = camTilePosition.tileY(); tilePositionOnMapY < camTilePosition.tileY() + camTilePosition.heightInTiles(); tilePositionOnMapY++) {

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
				Position tilePosition = Position.fromCoordinates(tilePositionOnMapX, tilePositionOnMapY, map.getMapType());
				Vector pieceVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(tilePosition, camTilePosition);

				render(map, (int)pieceVectorRelatedToCam.getX(), (int)pieceVectorRelatedToCam.getY(), tilePosition);
			}
		}


		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());


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
