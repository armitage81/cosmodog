package antonafanasjew.cosmodog.ingamemenu.map;

import java.util.Date;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterUtils;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class MapRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		
		//Keep this block as you will need it to regenerate the map if it changes.
		
		/*
		if (mapCache == null) {
			try {
				CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
				mapCache = new Image(map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
				Graphics mapCacheGraphics = mapCache.getGraphics();
				//This context starts with 0/0 as it relates to the map image graphics.
				DrawingContext mapDrawingContext = new SimpleDrawingContext(null, 0, 0, (float)mapCache.getWidth(), (float)mapCache.getHeight());
				
				for (int i = 0; i < Layers.LAYER_META_COLLISIONS; i++) {
					for (int tx = 0; tx < map.getWidth(); tx++) {
						for (int ty = 0; ty < map.getHeight(); ty++) {
							TileDrawingContext tileDc = new TileDrawingContext(mapDrawingContext, map.getWidth(), map.getHeight(), tx, ty);
							if (tx >= 0 && ty >= 0 && tx < map.getWidth() && ty < map.getHeight()) {
								render(map, tileDc, mapCacheGraphics, tx, ty, i);
							}
						}
					}
					
				}
				
				ImageOut.write(mapCache, "c:/temp/map.png");
				
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		*/
		
		int mapPieceColumns = ChartInventoryItem.CHART_PIECE_NUMBER_X;
		int mapPieceRows = ChartInventoryItem.CHART_PIECE_NUMBER_Y;
		
		Image mapImage = ApplicationContext.instance().getAnimations().get("completechart").getImage(0);
		
		DrawingContext contentContext = new CenteredDrawingContext(drawingContext, drawingContext.w() - 10, drawingContext.h() - 10);
		
		DrawingContext mapAreaDrawingContext = mapAreaDrawingContext(contentContext);
		DrawingContext descriptionDrawingContext = descriptionDrawingContext(contentContext);
		DrawingContext coordinatesDrawingContext = new TileDrawingContext(descriptionDrawingContext, 1, 6, 0, 0);
		DrawingContext hintsDrawingContext = new TileDrawingContext(descriptionDrawingContext, 1, 6, 0, 1, 1, 2);
		DrawingContext fullMapDrawingContext = new TileDrawingContext(descriptionDrawingContext, 1, 6, 0, 3, 1, 3);
		
		MapInputState mapInputState = (MapInputState)renderingParameter;
		
		int mapVisibleAreaX = mapInputState.getSelectionX();
		int mapVisibleAreaY = mapInputState.getSelectionY();
		
		
		Player player = ApplicationContextUtils.getPlayer();
		ChartInventoryItem chartInventoryItem = (ChartInventoryItem)player.getInventory().get(InventoryItemType.CHART);
		
		boolean chartPieceDiscovered = chartInventoryItem != null && chartInventoryItem.pieceIsDiscovered(mapVisibleAreaX, mapVisibleAreaY);

		float mapAreaContextLength = mapAreaDrawingContext.w() < mapAreaDrawingContext.h() ? mapAreaDrawingContext.w() : mapAreaDrawingContext.h();
		DrawingContext mapAreaQuadraticDrawingContext = new CenteredDrawingContext(mapAreaDrawingContext, mapAreaContextLength, mapAreaContextLength);
		
		if (chartPieceDiscovered) {
		
			float pieceWidth = mapImage.getWidth() / (float)mapPieceColumns;
			float pieceHeight = mapImage.getHeight() / (float)mapPieceRows;
			
			float offsetX = pieceWidth * mapVisibleAreaX;
			float offsetY = pieceHeight * mapVisibleAreaY;
			

			
			graphics.drawImage(mapImage, mapAreaQuadraticDrawingContext.x(), mapAreaQuadraticDrawingContext.y(), mapAreaQuadraticDrawingContext.x() + mapAreaQuadraticDrawingContext.w(), mapAreaQuadraticDrawingContext.y() + mapAreaQuadraticDrawingContext.h(), offsetX, offsetY, offsetX + pieceWidth, offsetY + pieceHeight);
		
		} else {
			LetterTextRenderer.getInstance().render(gameContainer, graphics, mapAreaQuadraticDrawingContext, LetterTextRenderingParameter.fromTextScaleFactorAndAlignment("No map data.", 3.0f, LetterTextRenderingParameter.HOR_ALIGNMENT_CENTER, LetterTextRenderingParameter.VER_ALIGNMENT_CENTER));
		}
		
		
		
		graphics.translate(mapAreaDrawingContext.x(), mapAreaDrawingContext.y());
		graphics.setColor(Color.white);
		graphics.drawRoundRect(0, 0, mapAreaDrawingContext.w(), mapAreaDrawingContext.h(), 5);
		graphics.translate(-mapAreaDrawingContext.x(), -mapAreaDrawingContext.y());
		
		graphics.translate(coordinatesDrawingContext.x(), coordinatesDrawingContext.y());
		graphics.setColor(Color.white);
		graphics.drawRoundRect(0, 0, coordinatesDrawingContext.w(), coordinatesDrawingContext.h(), 5);
		graphics.translate(-coordinatesDrawingContext.x(), -coordinatesDrawingContext.y());
		
		graphics.translate(hintsDrawingContext.x(), hintsDrawingContext.y());
		graphics.setColor(Color.white);
		graphics.drawRoundRect(0, 0, hintsDrawingContext.w(), hintsDrawingContext.h(), 5);
		graphics.translate(-hintsDrawingContext.x(), -hintsDrawingContext.y());
		
		graphics.translate(fullMapDrawingContext.x(), fullMapDrawingContext.y());
		graphics.setColor(Color.white);
		graphics.drawRoundRect(0, 0, fullMapDrawingContext.w(), fullMapDrawingContext.h(), 5);
		graphics.translate(-fullMapDrawingContext.x(), -fullMapDrawingContext.y());
		
		coordinatesDrawingContext = new CenteredDrawingContext(coordinatesDrawingContext, 20);
		
		StringBuffer mapPieceDescription = new StringBuffer(String.valueOf(mapInputState.getSelectionX() + 1) + "/" + String.valueOf(mapInputState.getSelectionY() + 1));
		if (!chartPieceDiscovered) {
			mapPieceDescription.append(" (Uncharted)");
		}
		
		List<String> textLines = LetterUtils.splitTextForTextBoxWidth(mapPieceDescription.toString(), 2f, coordinatesDrawingContext.w());

		float lineHeight = Letter.LETTER_HEIGHT * 2f;
		
		int lines = (int)(coordinatesDrawingContext.h() / lineHeight);
		
		for (int i = 0; i < textLines.size(); i++) {
			DrawingContext lineDc = new TileDrawingContext(coordinatesDrawingContext, 1, lines, 0, i);
			LetterTextRenderer.getInstance().render(gameContainer, graphics, lineDc, LetterTextRenderingParameter.fromTextAndScaleFactor(textLines.get(i), 2f));
		}
		
		
		hintsDrawingContext = new CenteredDrawingContext(hintsDrawingContext, 20);
		
		String hints = "Use arrow keys to scroll through the map. Find map pieces to chart the map.";
		textLines = LetterUtils.splitTextForTextBoxWidth(hints, 2f, hintsDrawingContext.w());

		lineHeight = Letter.LETTER_HEIGHT * 2f;
		
		lines = (int)(hintsDrawingContext.h() / lineHeight);
		
		for (int i = 0; i < textLines.size(); i++) {
			DrawingContext lineDc = new TileDrawingContext(hintsDrawingContext, 1, lines, 0, i);
			LetterTextRenderer.getInstance().render(gameContainer, graphics, lineDc, LetterTextRenderingParameter.fromTextAndScaleFactor(textLines.get(i), 2f));
		}
		
		
		DrawingContext fullMapGridDc = new CenteredDrawingContext(fullMapDrawingContext, 20);
		
		float gridW = fullMapGridDc.w() / mapPieceColumns;
		float gridH = fullMapGridDc.h() / mapPieceRows;
		
		graphics.translate(fullMapGridDc.x(), fullMapGridDc.y());
		
		for (int i = 0; i < mapPieceColumns; i++) {
			for (int j = 0; j < mapPieceRows; j++) {
				float x = gridW * i;
				float y = gridH * j;
				boolean currentSelection = (i == mapVisibleAreaX) && (j == mapVisibleAreaY);
				boolean discovered = chartInventoryItem != null && chartInventoryItem.pieceIsDiscovered(i, j);
				if (discovered) {
					graphics.setColor(Color.blue);
				} else {
					graphics.setColor(Color.red);
				}
				graphics.fillRect(x, y, gridW, gridH);
				graphics.setColor(Color.white);
				graphics.drawRect(x, y, gridW, gridH);
				
				long timestamp = System.currentTimeMillis();
				if (currentSelection && (timestamp / 250) % 2 == 0) {
					graphics.setColor(Color.orange);
					graphics.fillRect(x, y, gridW, gridH);
				}
			}
		}
		
		graphics.translate(-fullMapGridDc.x(), -fullMapGridDc.y());
		
	}
	
	
//	private void render(CosmodogMap map, DrawingContext dc, Graphics g, int tilePosX, int tilePosY, int layerIndex) {
//		
//		
//		Animation animation = null;
//
//		int tileId = map.getTileId(tilePosX, tilePosY, layerIndex);
//
//		int imageIndex = tileId - 1;
//
//		if (imageIndex >= 0) {
//
//			int imagePosX = imageIndex % 9;
//			int imagePosY = imageIndex / 9;
//
//			SpriteSheet tilesetSpriteSheet = ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_TILES);
//
//			Image tileImage = tilesetSpriteSheet.getSprite(imagePosX, imagePosY);
//			g.drawImage(tileImage, dc.x(), dc.y(), dc.x() + dc.w(), dc.y() + dc.h(), 0, 0, tileImage.getWidth(), tileImage.getHeight());
//		}
//
//	}
	
	private DrawingContext mapAreaDrawingContext(DrawingContext mainDc) {
		return new TileDrawingContext(mainDc, 3, 1, 0, 0, 2, 1);
	}
	
	private DrawingContext descriptionDrawingContext(DrawingContext mainDc) {
		return new TileDrawingContext(mainDc, 3, 1, 2, 0, 1, 1);
	}

}
