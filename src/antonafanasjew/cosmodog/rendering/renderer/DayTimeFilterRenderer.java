package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.movement.CrossTileMotion;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.maprendererpredicates.MapLayerRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.renderingutils.ActorRendererUtils;
import antonafanasjew.cosmodog.sight.VisibilityCalculatorForPlayer;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PositionUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

import java.util.Set;

public class DayTimeFilterRenderer extends AbstractRenderer {

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		//Start drawing at the beginning of the scene. Usually, it is the top left corner of the screen.
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();

		Cam cam = cosmodogGame.getCam();

		//The original tile width and height. They are usually 16x16 pixels big.
		int tileLength = TileUtils.tileLengthSupplier.get();

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



		float crossTileOffsetX = 0;
		float crossTileOffsetY = 0;

		CrossTileMotion motion = ActorRendererUtils.actorMotion(player);

		if (motion != null) {
			crossTileOffsetX = motion.getCrossTileOffsetX();
			crossTileOffsetY = motion.getCrossTileOffsetY();
		}

		PlanetaryCalendar calendar = ApplicationContext.instance().getCosmodog().getCosmodogGame().getPlanetaryCalendar();

		Color filterColor = new Color(0f,0f,0f,0f);
		Color filterColorNearby = new Color(0f,0f,0f,0f);

		if (calendar.isEvening()) {
			filterColor = new Color(1f,0f, 0f, 0.2f);
			filterColorNearby = filterColor;
		} else if (calendar.isNight()) {
			filterColor = new Color(0f,0f, 1f, 0.5f);
			filterColorNearby = new Color(0f,0f, 0f, 0.3f);
		} else if (calendar.isMorning()) {
			filterColor = new Color(1f,0f,0f,0.4f);
			filterColorNearby = filterColor;
		}

		int grace = 2;
		//We paint each tile individually as 1*1 tile map section to be flexible on skipping and replacing tiles.
		for (int tilePositionOnMapX = camPositionOnMapInTilesX - grace; tilePositionOnMapX < camPositionOnMapInTilesX + camViewWidthInTiles + 2 * grace; tilePositionOnMapX++) {
			for (int tilePositionOnMapY = camPositionOnMapInTilesY - grace; tilePositionOnMapY < camPositionOnMapInTilesY + camViewHeightInTiles + 2 * grace; tilePositionOnMapY++) {

				Position tilePosition = Position.fromCoordinates(tilePositionOnMapX, tilePositionOnMapY, map.getMapDescriptor());

				Color colorToUse = filterColor;

				if (VisibilityCalculatorForPlayer.instance().visible(player, map, calendar, tilePosition)) {
					colorToUse = filterColorNearby;
				}
				graphics.setColor(colorToUse);

				float rectX = (tilePositionOnMapX - camPositionOnMapInTilesX) * tileLength + (tileLength * crossTileOffsetX);
				float rectY = (tilePositionOnMapY - camPositionOnMapInTilesY) * tileLength + (tileLength * crossTileOffsetY);
				float rectW = tileLength;
				float rectH = tileLength;

				graphics.fillRect(rectX, rectY, rectW, rectH);
			}
		}


		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(camOffsetFromTileBeginX, camOffsetFromTileBeginY);

		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());

	}

}
