package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SupplyTrackerInventoryItem;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.PositionUtils;

public class SupplyTrackerViewRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics g, Object renderingParameter) {
		
		if (Features.getInstance().featureOn(Features.FEATURE_INTERFACE) == false) {
			return;
		}
		
		Player player = ApplicationContextUtils.getPlayer();
		
		SupplyTrackerInventoryItem supplyTracker = (SupplyTrackerInventoryItem)player.getInventory().get(InventoryItemType.SUPPLYTRACKER);
		
		if (supplyTracker != null) {
		
			DrawingContext supplyTrackerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().supplyTrackerDrawingContext();
			DrawingContext viewDc = new CenteredDrawingContext(supplyTrackerDrawingContext, 5);

			Piece closestSupply = PlayerMovementCache.getInstance().getClosestSupply();
			
			if (closestSupply != null) {
				float angle = PositionUtils.exactTargetDirection(player, closestSupply);

				Image image = ApplicationContext.instance().getImages().get("ui.ingame.compasspointer");
				ImageUtils.renderImageRotated(gameContainer, g, image, viewDc, angle);
				

			}
		
		}
		
	}

}
