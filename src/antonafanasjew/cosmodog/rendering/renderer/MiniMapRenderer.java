package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.*;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.ingamemenu.map.MapInputState;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TilesetUtils;

import java.util.Map;

public class MiniMapRenderer implements Renderer {

	private DrawingContext drawingContext;

	public MiniMapRenderer(DrawingContext drawingContext) {
		this.drawingContext = drawingContext;
	}

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		MapInputState mapInputState = (MapInputState) renderingParameter;

		Animation unchartedMapTileAnimation = ApplicationContext.instance().getAnimations().get("unchartedMapTile");

		int columnToRender = mapInputState.getSelectionX();
		int rowToRender = mapInputState.getSelectionY();
		float offsetX = mapInputState.getOffsetX();
		float offsetY = mapInputState.getOffsetY();

		int miniMapColumns = ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_X;
		int miniMapRows = ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_Y;

		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();

		int mapWidthInTiles = map.getWidth();
		int mapHeightInTiles = map.getHeight();

		int minimapPieceWidthInTiles = mapWidthInTiles / miniMapColumns;
		int minimapPieceHeightInTiles = mapHeightInTiles / miniMapRows;

		int firstTileToRenderX = minimapPieceWidthInTiles * columnToRender + (int) (minimapPieceWidthInTiles * offsetX);
		int firstTileToRenderY = minimapPieceHeightInTiles * rowToRender + (int) (minimapPieceHeightInTiles * offsetY);

		Player player = ApplicationContextUtils.getPlayer();
		ChartInventoryItem chartInventoryItem = (ChartInventoryItem) player.getInventory().get(InventoryItemType.CHART);

		for (int i = 0; i < Layers.LAYER_META_COLLISIONS; i++) {

			for (int tx = firstTileToRenderX; tx < firstTileToRenderX + minimapPieceWidthInTiles; tx++) {
				for (int ty = firstTileToRenderY; ty < firstTileToRenderY + minimapPieceHeightInTiles; ty++) {
					if (tx >= 0 && ty >= 0 && tx < map.getWidth() && ty < map.getHeight()) {

						int chartPiecePositionX = tx / minimapPieceWidthInTiles;
						int chartPiecePositionY = ty / minimapPieceHeightInTiles;

						int tileId = map.getTileId(tx, ty, i);

						int imageIndex = tileId - 1;

						if (imageIndex >= 0) {

							int imagePosX = imageIndex % 9;
							int imagePosY = imageIndex / 9;

							Image tileImage = TilesetUtils.tileByIndex(imagePosX, imagePosY);

							DrawingContext tileDc = new TileDrawingContext(drawingContext, minimapPieceWidthInTiles, minimapPieceHeightInTiles, tx - firstTileToRenderX, ty - firstTileToRenderY);
							if (chartInventoryItem != null && chartInventoryItem.pieceIsDiscovered(chartPiecePositionX, chartPiecePositionY)) {
								tileImage.draw(tileDc.x(), tileDc.y(), tileDc.w(), tileDc.h());
							} else {
								unchartedMapTileAnimation.draw(tileDc.x(), tileDc.y(), tileDc.w(), tileDc.h());
							}
						}
					}
				}
			}
		}

		boolean evenPhaseOfBlinking = System.currentTimeMillis() / 200 % 2 == 0;

		Map<Position, Piece> insights = map.getInsights();
		for (Position insightPosition : insights.keySet()) {
			int x = (int)insightPosition.getX();
			int y = (int)insightPosition.getY();

			if (x < firstTileToRenderX) {
				continue;
			}
			if (x >= firstTileToRenderX + minimapPieceWidthInTiles) {
				continue;
			}
			if (y < firstTileToRenderY) {
				continue;
			}
			if (y >= firstTileToRenderY + minimapPieceHeightInTiles) {
				continue;
			}

			DrawingContext tileDc = new TileDrawingContext(drawingContext, minimapPieceWidthInTiles, minimapPieceHeightInTiles, x - firstTileToRenderX, y - firstTileToRenderY);
			graphics.setColor(evenPhaseOfBlinking ? Color.red: Color.orange);
			graphics.fillRect(tileDc.x(), tileDc.y(), tileDc.w(), tileDc.h());
		}

	}

}
