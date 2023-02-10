package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class GeigerCounterViewRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics g, Object renderingParameter) {
		
		//This is the small square at the top right of the interface
		DrawingContext geigerCounterDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().geigerCounterDrawingContext();
		
		
		if (Features.getInstance().featureOn(Features.FEATURE_INTERFACE) == false) {
			return;
		}
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();	

		//Calculate radiation around the player.
		boolean radiationAdjacent = false;
		boolean[][] radiationInfos = new boolean[3][3];
		int posX = player.getPositionX();
		int posY = player.getPositionY();
		int xMin = posX - 1;
		int xMax = posX + 1;
		int yMin = posY - 1;
		int yMax = posY + 1;
		for (int i = xMin; i <= xMax; i++) {
			for (int j = yMin; j <= yMax; j++) {
				radiationInfos[i - xMin][j - yMin] = false;
				int radiationTileId = map.getTileId(i, j, Layers.LAYER_META_RADIATION);
				if (TileType.RADIATION.getTileId() == radiationTileId) {
					radiationInfos[i - xMin][j - yMin] = true;
					radiationAdjacent = true;
				}
			}
		}

		long timestamp = System.currentTimeMillis();
		
		//Does the player have the Geiger counter?
		GeigerZaehlerInventoryItem geigerZaehler = (GeigerZaehlerInventoryItem)player.getInventory().get(InventoryItemType.GEIGERZAEHLER);
		
		if (geigerZaehler != null) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					Color color;
					if (radiationInfos[i][j] == true) {
						if ((timestamp / 250) % 2 == 0) {
							color = Color.red;
						} else {
							color = Color.white;
						}
					} else {
						color = Color.green;	
					}
					g.setColor(color);
					TileDrawingContext tileDc = new TileDrawingContext(geigerCounterDrawingContext, 3, 3, i, j);
					g.fillRect(tileDc.x(), tileDc.y(), tileDc.w(), tileDc.h());
					g.setColor(Color.white);
					g.setLineWidth(1);
					g.drawRect(tileDc.x(), tileDc.y(), tileDc.w(), tileDc.h());
				}
			}
			TileDrawingContext tileDc = new TileDrawingContext(geigerCounterDrawingContext, 3, 3, 1, 1);
			g.setColor(Color.orange);
			g.fillRect(tileDc.x(), tileDc.y(), tileDc.w(), tileDc.h());
			g.setColor(Color.white);
			g.setLineWidth(1);
			g.drawRect(tileDc.x(), tileDc.y(), tileDc.w(), tileDc.h());
		} else { 
			if (radiationAdjacent) {
				Color color;
				if ((timestamp / 250) % 2 == 0) {
					color = Color.red;
				} else {
					color = Color.white;
				}
				g.setColor(color);
				g.fillRect(geigerCounterDrawingContext.x(), geigerCounterDrawingContext.y(), geigerCounterDrawingContext.w(), geigerCounterDrawingContext.h());
			}
		}
		
	}

}
