package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SupplyTrackerInventoryItem;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.PositionUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class SupplyTrackerViewRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics g, DrawingContext drawingContext, Object renderingParameter) {
		
//		LetterTextRenderer.getInstance().render(gameContainer, g, supplyTrackerLabelDrawingContext, LetterTextRenderingParameter.fromText("BOX"));
//
//		String supplyTrackerValue = "--";
//		
//		SupplyTrackerInventoryItem supplyTracker = (SupplyTrackerInventoryItem)player.getInventory().get(InventoryItemType.SUPPLYTRACKER);
//		
//		if (supplyTracker != null) {
//			
//			Piece closestSupply = PlayerMovementCache.getInstance().getClosestSupply();
//			
//			if (closestSupply != null) {
//				DirectionType dirType = PositionUtils.targetDirection(player, closestSupply);
//				supplyTrackerValue = dirType.getRepresentation().toUpperCase();
//			}
//			
//		}		
//		LetterTextRenderer.getInstance().render(gameContainer, g, supplyTrackerValueDrawingContext, LetterTextRenderingParameter.fromText(supplyTrackerValue));
		
		Player player = ApplicationContextUtils.getPlayer();
		
		SupplyTrackerInventoryItem supplyTracker = (SupplyTrackerInventoryItem)player.getInventory().get(InventoryItemType.SUPPLYTRACKER);
		
		if (supplyTracker != null) {
		
			DrawingContext viewDc = new CenteredDrawingContext(drawingContext, 5);

			Piece closestSupply = PlayerMovementCache.getInstance().getClosestSupply();
			
			if (closestSupply != null) {
				float angle = PositionUtils.exactTargetDirection(player, closestSupply);

				Image image = ApplicationContext.instance().getImages().get("ui.ingame.compasspointer");
				ImageUtils.renderImageRotated(gameContainer, g, image, viewDc, angle);
				

			}
		
		}
		
	}

}
