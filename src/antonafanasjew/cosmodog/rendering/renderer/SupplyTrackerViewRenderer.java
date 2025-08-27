package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SupplyTrackerInventoryItem;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PositionUtils;

public class SupplyTrackerViewRenderer extends AbstractRenderer {

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics g, Object renderingParameter) {
		
		Player player = ApplicationContextUtils.getPlayer();
		
		SupplyTrackerInventoryItem supplyTracker = (SupplyTrackerInventoryItem)player.getInventory().get(InventoryItemType.SUPPLYTRACKER);
		
		if (supplyTracker != null) {
		
			DrawingContext supplyTrackerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().supplyTrackerDrawingContext();

			Piece closestSupply = PlayerMovementCache.getInstance().getClosestSupply();
			Piece closestMedkit = PlayerMovementCache.getInstance().getClosestMedkit();
			
			float centerX = supplyTrackerDrawingContext.x() + supplyTrackerDrawingContext.w() / 2.0f;
			float centerY = supplyTrackerDrawingContext.y() + supplyTrackerDrawingContext.h() / 2.0f; 

			g.setLineWidth(2);

			if (closestSupply != null) {
				float angle = PositionUtils.exactTargetDirection(player, closestSupply);
				float degree = (float)(angle * 180 / Math.PI);
				g.rotate(centerX, centerY, degree);
				g.setColor(Color.red);
				g.drawLine(centerX, centerY, centerX, supplyTrackerDrawingContext.y());
				g.rotate(centerX, centerY, -degree);
			}
			
			if (closestMedkit != null) {
				float angle = PositionUtils.exactTargetDirection(player, closestMedkit);
				float degree = (float)(angle * 180 / Math.PI);
				g.rotate(centerX, centerY, degree);
				g.setColor(Color.green);
				g.drawLine(centerX, centerY, centerX, supplyTrackerDrawingContext.y());
				g.rotate(centerX, centerY, -degree);
			}
			
			g.setColor(Color.blue);
			g.fillOval(centerX - 5, centerY - 5, 10, 10);
		
		}
		
	}

}
