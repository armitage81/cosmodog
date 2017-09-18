package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.ingamemenu.map.MapInputState;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TilesetUtils;



public class MiniMapRenderer implements Renderer {
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		MapInputState mapInputState = (MapInputState)renderingParameter;
		
		int columnToRender = mapInputState.getSelectionX();
		int rowToRender = mapInputState.getSelectionY();
		
		
		int miniMapColumns = ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_X;
		int miniMapRows = ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_Y;
		
		
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();

		int mapWidthInTiles = map.getWidth();
		int mapHeightInTiles = map.getHeight();
		
		int minimapPieceWidthInTiles = mapWidthInTiles / miniMapColumns;
		int minimapPieceHeightInTiles = mapHeightInTiles / miniMapRows;
		
		int firstTileToRenderX = minimapPieceWidthInTiles * columnToRender;
		int firstTileToRenderY = minimapPieceHeightInTiles * rowToRender;
			
		for (int i = 0; i < Layers.LAYER_META_COLLISIONS; i++) {

			for (int tx = firstTileToRenderX; tx < firstTileToRenderX + minimapPieceWidthInTiles; tx++) {
				for (int ty = firstTileToRenderY; ty < firstTileToRenderY + minimapPieceHeightInTiles; ty++) {
					if (tx >= 0 && ty >= 0 && tx < map.getWidth() && ty < map.getHeight()) {

						int tileId = map.getTileId(tx, ty, i);

						int imageIndex = tileId - 1;
						
						if (imageIndex >= 0) {
						
							int imagePosX = imageIndex % 9;
							int imagePosY = imageIndex / 9;
							
							Image tileImage = TilesetUtils.tileByIndex(imagePosX, imagePosY);
							
							DrawingContext tileDc = new TileDrawingContext(drawingContext, minimapPieceWidthInTiles, minimapPieceHeightInTiles, tx - firstTileToRenderX, ty - firstTileToRenderY);
							
							tileImage.draw(tileDc.x(), tileDc.y(), tileDc.w(), tileDc.h());
						}	
					}
				}
			}
			
		}
		
	}

}
