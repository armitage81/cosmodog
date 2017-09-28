package antonafanasjew.cosmodog.ingamemenu.map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.globals.ResolutionHolder;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.MiniMapRenderer;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.DrawingContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class MapRenderer implements Renderer {

	
	private MiniMapRenderer miniMapRenderer = new MiniMapRenderer();
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
				
		ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.ingamemap", drawingContext);
		
		int actualMapPieceColumns = ChartInventoryItem.ACTUAL_CHART_PIECE_NUMBER_X;
		int actualMapPieceRows = ChartInventoryItem.ACTUAL_CHART_PIECE_NUMBER_Y;
		
		int mapPieceColumns = ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_X;
		int mapPieceRows = ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_Y;
		
		DrawingContext mapAreaDrawingContext = mapAreaDrawingContext(drawingContext);
		DrawingContext descriptionDrawingContext = descriptionDrawingContext(drawingContext);
		DrawingContext coordinatesDrawingContext = new TileDrawingContext(descriptionDrawingContext, 1, 6, 0, 0);
		DrawingContext hintsDrawingContext = new TileDrawingContext(descriptionDrawingContext, 1, 6, 0, 1, 1, 2);
		DrawingContext fullMapDrawingContext = new TileDrawingContext(descriptionDrawingContext, 1, 6, 0, 3, 1, 3);
		
		MapInputState mapInputState = (MapInputState)renderingParameter;
		
		int mapVisibleAreaX = mapInputState.getSelectionX();
		int mapVisibleAreaY = mapInputState.getSelectionY();
		
		
		Player player = ApplicationContextUtils.getPlayer();
		ChartInventoryItem chartInventoryItem = (ChartInventoryItem)player.getInventory().get(InventoryItemType.CHART);
		
		boolean chartPieceDiscovered = chartInventoryItem != null && chartInventoryItem.pieceIsDiscovered(mapVisibleAreaX / 2, mapVisibleAreaY / 2);

		float mapAreaContextLength = mapAreaDrawingContext.w() < mapAreaDrawingContext.h() ? mapAreaDrawingContext.w() : mapAreaDrawingContext.h();
		DrawingContext mapAreaQuadraticDrawingContext = new CenteredDrawingContext(mapAreaDrawingContext, mapAreaContextLength, mapAreaContextLength);
		
		if (chartPieceDiscovered) {
		
			miniMapRenderer.render(gameContainer, graphics, mapAreaQuadraticDrawingContext, mapInputState);
			
		}
		
		//Render players position in the selected chart piece.
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
		int posX = player.getPositionX();
		int posY = player.getPositionY();
		
		int chartPieceWidth = mapWidth / ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_X;
		int chartPieceHeight = mapHeight / ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_Y;
		
		if (mapVisibleAreaX == posX / chartPieceWidth && mapVisibleAreaY == posY / chartPieceHeight) {
			int posInChartPieceX = posX - (chartPieceWidth * mapVisibleAreaX);
			int posInChartPieceY = posY - (chartPieceHeight * mapVisibleAreaY);
			
			TileDrawingContext playerPositionDrawingContext = new TileDrawingContext(mapAreaQuadraticDrawingContext, chartPieceWidth, chartPieceHeight, posInChartPieceX, posInChartPieceY);
			
			long timestamp = System.currentTimeMillis();
			if ((timestamp / 100) % 2 == 0) {
				graphics.setColor(Color.orange);
			} else {
				graphics.setColor(Color.red);
			}
			graphics.fillRect(playerPositionDrawingContext.x(), playerPositionDrawingContext.y(), playerPositionDrawingContext.w(), playerPositionDrawingContext.h());
		}
		
		
		
		
		
		coordinatesDrawingContext = new CenteredDrawingContext(coordinatesDrawingContext, 20);
		
		StringBuffer mapPieceDescription = new StringBuffer(String.valueOf(mapInputState.getSelectionX() + 1) + "/" + String.valueOf(mapInputState.getSelectionY() + 1));
		if (!chartPieceDiscovered) {
			mapPieceDescription.append(" (Uncharted)");
		}
		
		String text = mapPieceDescription.toString();
		TextBookRendererUtils.renderTextPage(gameContainer, graphics, coordinatesDrawingContext, text, FontType.InGameMenuInterface, 0);
		
		
		hintsDrawingContext = new CenteredDrawingContext(hintsDrawingContext, 20);
		
		text = "Use arrow keys to scroll through the map. Find map pieces to chart the map.";
		TextBookRendererUtils.renderTextPage(gameContainer, graphics, hintsDrawingContext, text, FontType.InGameMenuInterface, 0);
		
		DrawingContext fullMapGridDc = new CenteredDrawingContext(fullMapDrawingContext, 20);
		
		float gridW = fullMapGridDc.w() / mapPieceColumns;
		float gridH = fullMapGridDc.h() / mapPieceRows;
		
		float bigGridW = fullMapGridDc.w() / actualMapPieceColumns;
		float bigGridH = fullMapGridDc.h() / actualMapPieceRows;
		
		graphics.translate(fullMapGridDc.x(), fullMapGridDc.y());
		
		for (int i = 0; i < mapPieceColumns; i++) {
			for (int j = 0; j < mapPieceRows; j++) {
				float x = gridW * i;
				float y = gridH * j;
				boolean currentSelection = (i == mapVisibleAreaX) && (j == mapVisibleAreaY);
				boolean discovered = chartInventoryItem != null && chartInventoryItem.pieceIsDiscovered(i / 2, j / 2);
				if (discovered) {
					graphics.setColor(Color.blue);
				} else {
					graphics.setColor(Color.red);
				}
				graphics.fillRect(x, y, gridW, gridH);
				//graphics.setColor(Color.orange);
				//graphics.drawRect(x, y, gridW, gridH);
				
				long timestamp = System.currentTimeMillis();
				if (currentSelection && (timestamp / 250) % 2 == 0) {
					graphics.setColor(Color.orange);
					graphics.fillRect(x, y, gridW, gridH);
				}
			}
		}
		
		for (int i = 0; i < actualMapPieceColumns; i++) {
			for (int j = 0; j < actualMapPieceRows; j++) {
				
				float x = bigGridW * i;
				float y = bigGridH * j;
				
				graphics.setColor(Color.orange);
				graphics.drawRect(x, y, bigGridW, bigGridH);				
			}
		}
		
		graphics.translate(-fullMapGridDc.x(), -fullMapGridDc.y());
	}
	
	private DrawingContext mapAreaDrawingContext(DrawingContext mainDc) {
		DrawingContext dc = new SimpleDrawingContext(null, 13 + 33, 13 + 144, 759, 406);
		dc = DrawingContextUtils.difResFromRef(dc, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		return dc;
	}
	
	private DrawingContext descriptionDrawingContext(DrawingContext mainDc) {
		DrawingContext dc = new SimpleDrawingContext(null, 804 + 33, 13 + 144, 397, 406);
		dc = DrawingContextUtils.difResFromRef(dc, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		return dc;
	}

}
