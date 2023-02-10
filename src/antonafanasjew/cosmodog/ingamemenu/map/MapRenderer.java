package antonafanasjew.cosmodog.ingamemenu.map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.globals.ResolutionHolder;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ArcheologistsJournalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.MiniMapRenderer;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.DrawingContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class MapRenderer implements Renderer {

	private DrawingContext inGameMenuContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().inGameMenuContentDrawingContext();
	private DrawingContext mapAreaDrawingContext = mapAreaDrawingContext(inGameMenuContentDrawingContext);
	private DrawingContext descriptionDrawingContext = descriptionDrawingContext(inGameMenuContentDrawingContext);
	//private DrawingContext coordinatesDrawingContext = new CenteredDrawingContext(new TileDrawingContext(descriptionDrawingContext, 1, 6, 0, 0), 20);
	private DrawingContext hintsDrawingContext = new CenteredDrawingContext(new TileDrawingContext(descriptionDrawingContext, 1, 6, 0, 1, 1, 2), 20);
	private DrawingContext fullMapDrawingContext = new TileDrawingContext(descriptionDrawingContext, 1, 6, 0, 3, 1, 3);
	private float mapAreaContextLength = mapAreaDrawingContext.w() < mapAreaDrawingContext.h() ? mapAreaDrawingContext.w() : mapAreaDrawingContext.h();
	private DrawingContext mapAreaQuadraticDrawingContext = new CenteredDrawingContext(mapAreaDrawingContext, mapAreaContextLength, mapAreaContextLength);
	private MiniMapRenderer miniMapRenderer = new MiniMapRenderer(mapAreaQuadraticDrawingContext);
	
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		//Render the frame
		DrawingContext inGameMenuContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().inGameMenuContentDrawingContext();
		ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.ingamemap", inGameMenuContentDrawingContext);
		
		int actualMapPieceColumns = ChartInventoryItem.ACTUAL_CHART_PIECE_NUMBER_X;
		int actualMapPieceRows = ChartInventoryItem.ACTUAL_CHART_PIECE_NUMBER_Y;
		int mapPieceColumns = ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_X;
		int mapPieceRows = ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_Y;
		MapInputState mapInputState = (MapInputState)renderingParameter;
		int mapVisiblePositionX = mapInputState.getSelectionX();
		int mapVisiblePositionY = mapInputState.getSelectionY();
		float offsetX = mapInputState.getOffsetX();
		float offsetY = mapInputState.getOffsetY();

		//Render the map section for the selected chart piece (if discovered).
		Player player = ApplicationContextUtils.getPlayer();
		ChartInventoryItem chartInventoryItem = (ChartInventoryItem)player.getInventory().get(InventoryItemType.CHART);
		float mapAreaContextLength = mapAreaDrawingContext.w() < mapAreaDrawingContext.h() ? mapAreaDrawingContext.w() : mapAreaDrawingContext.h();
		DrawingContext mapAreaQuadraticDrawingContext = new CenteredDrawingContext(mapAreaDrawingContext, mapAreaContextLength, mapAreaContextLength);
		miniMapRenderer.render(gameContainer, graphics, mapInputState);
		
		//Render players position in the selected chart piece.
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
		int posX = player.getPositionX();
		int posY = player.getPositionY();
		int chartPieceWidth = mapWidth / ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_X;
		int chartPieceHeight = mapHeight / ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_Y;
		int visibleXFrom = (int)((mapVisiblePositionX + offsetX) * chartPieceWidth);
		int visibleXTo = visibleXFrom + chartPieceWidth;
		int visibleYFrom = (int)((mapVisiblePositionY + offsetY) * chartPieceHeight);
		int visibleYTo = visibleYFrom + chartPieceHeight;
		if (posX >= visibleXFrom && posX < visibleXTo && posY >= visibleYFrom && posY < visibleYTo) {
			int posInChartPieceX = posX - visibleXFrom;
			int posInChartPieceY = posY - visibleYFrom;
			TileDrawingContext playerPositionDrawingContext = new TileDrawingContext(mapAreaQuadraticDrawingContext, chartPieceWidth, chartPieceHeight, posInChartPieceX, posInChartPieceY);
			long timestamp = System.currentTimeMillis();
			if ((timestamp / 100) % 2 == 0) {
				graphics.setColor(Color.orange);
			} else {
				graphics.setColor(Color.red);
			}
			graphics.fillRect(playerPositionDrawingContext.x(), playerPositionDrawingContext.y(), playerPositionDrawingContext.w(), playerPositionDrawingContext.h());
		}
		
		String text = "Use arrow keys to scroll through the map. Find map pieces to chart the map.";
		FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
		Book textBook = TextPageConstraints.fromDc(hintsDrawingContext).textToBook(text, fontRefToFontTypeMap);
		TextBookRendererUtils.renderDynamicTextPage(gameContainer, graphics, textBook);
		
		DrawingContext fullMapGridDc = new CenteredDrawingContext(fullMapDrawingContext, 20);
		float gridW = fullMapGridDc.w() / mapPieceColumns;
		float gridH = fullMapGridDc.h() / mapPieceRows;
		float bigGridW = fullMapGridDc.w() / actualMapPieceColumns;
		float bigGridH = fullMapGridDc.h() / actualMapPieceRows;
		graphics.translate(fullMapGridDc.x(), fullMapGridDc.y());
		
		//Render the map overview (without the orange grid).
		ArcheologistsJournalInventoryItem archeologistsJournal = (ArcheologistsJournalInventoryItem)player.getInventory().get(InventoryItemType.ARCHEOLOGISTS_JOURNAL);
		for (int i = 0; i < mapPieceColumns; i++) {
			for (int j = 0; j < mapPieceRows; j++) {
				float x = gridW * i;
				float y = gridH * j;
				boolean discovered = chartInventoryItem != null && chartInventoryItem.pieceIsDiscovered(i, j);
				if (discovered) {
					graphics.setColor(Color.blue);
				} else {
					graphics.setColor(Color.red);
				}
				graphics.fillRect(x, y, gridW, gridH);
				if (discovered && archeologistsJournal != null) {
					DrawingContext cellDc = new SimpleDrawingContext(null, x, y, gridW, gridH);
					Integer valueOfInfobitsForChartPiece = mapInputState.getInfobitNumbersByChartPiece().get(Position.fromCoordinates(i, j));
					valueOfInfobitsForChartPiece = valueOfInfobitsForChartPiece == null ? 0 : valueOfInfobitsForChartPiece;
					String infobitValueText = "" + valueOfInfobitsForChartPiece;
					fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.InformationalSmall);
					Book infobitValueBook = TextPageConstraints.fromDc(cellDc).textToBook(infobitValueText, fontRefToFontTypeMap);
					TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, infobitValueBook);
				}
			}
		}
				
		//Render the map grid.
		for (int i = 0; i < actualMapPieceColumns; i++) {
			for (int j = 0; j < actualMapPieceRows; j++) {
				float x = bigGridW * i;
				float y = bigGridH * j;
				graphics.setColor(Color.orange);
				graphics.drawRect(x, y, bigGridW, bigGridH);				
			}
		}
		
		//Render the cursor.
		long timestamp = System.currentTimeMillis();
		if ((timestamp / 250) % 2 == 0) {
			graphics.setColor(Color.orange);
			graphics.fillRect(mapVisiblePositionX * gridW + (gridW * offsetX), mapVisiblePositionY * gridH + (gridH * offsetY), gridW, gridH);
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
