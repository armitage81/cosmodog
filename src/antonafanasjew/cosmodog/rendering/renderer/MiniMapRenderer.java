package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.SpriteSheets;
import antonafanasjew.cosmodog.caching.PiecePredicates;
import antonafanasjew.cosmodog.geometry.Oscillations;
import antonafanasjew.cosmodog.globals.FontProvider;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.*;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.RegionUtils;
import org.newdawn.slick.*;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.ingamemenu.map.MapInputState;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TilesetUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Renders the minimap in the map screen.
 * <p>
 * Take note: Rendering the map happens in a similar way as rendering the actual game. There is no map image.
 * Instead, the map excerpt is rendered tile by tile.
 */
public class MiniMapRenderer extends AbstractRenderer {

	/**
	 * The drawing context in which the minimap is rendered.
	 */
	private final DrawingContext drawingContext;

	/**
	 * Creates a new minimap renderer.
	 *
	 * @param drawingContext The drawing context in which the minimap is rendered.
	 */
	public MiniMapRenderer(DrawingContext drawingContext) {
		this.drawingContext = drawingContext;
	}

	/**
	 * Renders the minimap.
	 *
	 * @param gameContainer - Not used. Needed only to fulfill the contract of the interface.
	 * @param graphics - The graphics object to execute rendering.
	 * @param renderingParameter - Contains the input state to know which part of the map to render depending on scrolling position.
	 */
	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		//Contains the input state to know which part of the map to render depending on scrolling position.
		MapInputState mapInputState = (MapInputState) renderingParameter;

		//This tile is rendered for undiscovered parts of the map.
		Animation unchartedMapTileAnimation = ApplicationContext.instance().getAnimations().get("unchartedMapTile");

		//There are 64 pieces of the chart in total. The map is divided into 8x8 pieces.
		int miniMapColumns = ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_X;
		int miniMapRows = ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_Y;

		//When scrolling, the visible part of the map is as big as one piece of the chart, but the scrolling happens in smaller steps.
		//This is why the offset is needed to know how much of the next piece is visible.
		//The address of the visible part is determined by selecting the position of a chart piece and adding the offset.
		int columnToRender = mapInputState.getSelectionX();
		int rowToRender = mapInputState.getSelectionY();
		float offsetX = mapInputState.getOffsetX();
		float offsetY = mapInputState.getOffsetY();

		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();

		int mapWidthInTiles = map.getMapType().getWidth();
		int mapHeightInTiles = map.getMapType().getHeight();

		//The map is 400 tiles long and 400 tiles wide. The minimap is 8x8 pieces of the chart.
		//This means that each piece of the chart is 50x50 tiles.
		int minimapPieceWidthInTiles = mapWidthInTiles / miniMapColumns;
		int minimapPieceHeightInTiles = mapHeightInTiles / miniMapRows;

		//The first tile to render horizontally is the column of the chart piece multiplied by the width of the piece
		//and then adding the offset.
		int firstTileToRenderX = minimapPieceWidthInTiles * columnToRender + (int) (minimapPieceWidthInTiles * offsetX);
		int firstTileToRenderY = minimapPieceHeightInTiles * rowToRender + (int) (minimapPieceHeightInTiles * offsetY);

		Player player = ApplicationContextUtils.getPlayer();
		ChartInventoryItem chartInventoryItem = (ChartInventoryItem) player.getInventory().get(InventoryItemType.CHART);

		//Rendering of the minimap happens by going through all visible layers and rendering the tiles in each of them.
		//Most top left tile is determined by the scrolling position.
		//Some tiles can be undiscovered depending on the found chart pieces.
		//They are rendered as uncharted by using the unchartedMapTileAnimation.
		//Tile sprites are taken from the same sprite sheet as in the actual game.

		Set<TiledObject> roofsOverPlayer = RegionUtils.roofsOverPiece(player, map);
		Set<TiledObject> roofRemovalBlockersOverPlayer = PlayerMovementCache.getInstance().getRoofRemovalBlockerRegionsOverPlayer();

		for (int i = 0; i < Layers.LAYER_META_COLLISIONS; i++) {

			boolean topLayer = i >= Layers.LAYER_FOREST_TOP;

			for (int tx = firstTileToRenderX; tx < firstTileToRenderX + minimapPieceWidthInTiles; tx++) {
				for (int ty = firstTileToRenderY; ty < firstTileToRenderY + minimapPieceHeightInTiles; ty++) {

					//Ignore tiles that are outside the visible excerpt.
					//This happens when the visible excerpt is at the edge of the map.
					if (tx >= 0 && ty >= 0 && tx < map.getMapType().getWidth() && ty < map.getMapType().getHeight()) {

						//On the map, all roofs that are over the player should not be rendered.
						if (topLayer && roofRemovalBlockersOverPlayer.isEmpty()) {

							Piece piece = new Piece();
							piece.setPosition(Position.fromCoordinates(tx, ty, map.getMapType()));
							Set<TiledObject> roofsOverTile = RegionUtils.roofsOverPiece(piece, map);
							roofsOverTile.retainAll(roofsOverPlayer);
							if (!roofsOverTile.isEmpty()) {
								continue;
							}
						}

						//The chart piece position for the tile is needed to determine if the tile is discovered.
						int chartPiecePositionX = tx / minimapPieceWidthInTiles;
						int chartPiecePositionY = ty / minimapPieceHeightInTiles;

						//The tile image for the given tile id is taken from the tileset sprite sheet.
						int tileId = map.getTileId(Position.fromCoordinates(tx, ty, map.getMapType()), i);

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

		List<Position> piecesIndicatedOnMap = map.getMapPieces().piecesOverall(PiecePredicates.PIECE_INDICATED_ON_MAP).stream().map(Piece::getPosition).toList();


		for (Position position : piecesIndicatedOnMap) {
			int x = (int)position.getX();
			int y = (int)position.getY();

			int chartPiecePositionX = x / minimapPieceWidthInTiles;
			int chartPiecePositionY = y / minimapPieceHeightInTiles;

			if (chartInventoryItem != null && chartInventoryItem.pieceIsDiscovered(chartPiecePositionX, chartPiecePositionY)) {

				//Ignore monoliths that are outside the visible excerpt.
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

				long timestamp = System.currentTimeMillis();
				float opacity = Oscillations.oscillation(timestamp, 0.2f, 1f, 1000, 0);

				DrawingContext tileDc = new TileDrawingContext(drawingContext, minimapPieceWidthInTiles, minimapPieceHeightInTiles, x - firstTileToRenderX, y - firstTileToRenderY);
				SpriteSheet symbolsSpriteSheet = ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_SYMBOLS);
				Image questionMark = symbolsSpriteSheet.getSprite(1, 0);
				questionMark.draw(tileDc.x() - tileDc.w(), tileDc.y() - tileDc.h(), tileDc.w() * 3, tileDc.h() * 3, new Color(1, 1, 1, opacity));
			}

		}

	}

}
