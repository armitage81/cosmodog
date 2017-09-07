package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class GeigerCounterViewRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics g, DrawingContext drawingContext, Object renderingParameter) {
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();	
		
		GeigerZaehlerInventoryItem geigerZaehler = (GeigerZaehlerInventoryItem)player.getInventory().get(InventoryItemType.GEIGERZAEHLER);
		
		if (geigerZaehler != null) {
		
			DrawingContext labelDc = new SimpleDrawingContext(null, 1111, 80, 56, 35);
			DrawingContext viewDc = drawingContext;
			
			boolean dangerIsClose = false;
			
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
						dangerIsClose = true;
					}
				}
			}
			
			long timestamp = System.currentTimeMillis();
			if (dangerIsClose) {
				if ((timestamp / 250) % 2 == 0) {
					TextBookRendererUtils.renderCenteredLabel(gameContainer, g, labelDc, "RADIATION!", FontType.RadiationLabelDanger, 0);
				}			
			}
			
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
					TileDrawingContext tileDc = new TileDrawingContext(viewDc, 3, 3, i, j);
					g.fillRect(tileDc.x(), tileDc.y(), tileDc.w(), tileDc.h());
					g.setColor(Color.white);
					g.setLineWidth(1);
					g.drawRect(tileDc.x(), tileDc.y(), tileDc.w(), tileDc.h());
				}
			}
		}
		
	}

}
