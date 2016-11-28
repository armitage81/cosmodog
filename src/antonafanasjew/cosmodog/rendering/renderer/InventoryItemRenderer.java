package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.Mappings;
import antonafanasjew.cosmodog.util.WritingRendererUtils;
import antonafanasjew.cosmodog.writing.dynamics.DynamicsTypes;
import antonafanasjew.cosmodog.writing.model.TextBlock;

public class InventoryItemRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		InventoryItem item = (InventoryItem)renderingParameter;
		
		/*
		 * 
		 * ARSENAL IS NOT RENDERED.
		 * 
		 * */
		if (item.getInventoryItemType().equals(InventoryItemType.ARSENAL)) {
			return;
		}
		
		DrawingContext paddedTileContext = new CenteredDrawingContext(drawingContext, drawingContext.w() - 5, drawingContext.h() - 5);
		
		String animationId = Mappings.INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.get(item.getInventoryItemType());
		Animation itemAnimation = ApplicationContext.instance().getAnimations().get(animationId);
		
		
		graphics.translate(paddedTileContext.x(), paddedTileContext.y());
		ApplicationContext.instance().getAnimations().get("inventoryItemBackground").draw(0, 0, paddedTileContext.w(), paddedTileContext.h());
		graphics.translate(-paddedTileContext.x(), -paddedTileContext.y());
		
		DrawingContext iconContext = new CenteredDrawingContext(paddedTileContext, 10);
		graphics.translate(iconContext.x(), iconContext.y());
		itemAnimation.draw(0, 0, iconContext.w(), iconContext.h());
		graphics.translate(-iconContext.x(), -iconContext.y());
		
		if (item instanceof VehicleInventoryItem) {
			Vehicle vehicle = ((VehicleInventoryItem)item).getVehicle();
			int fuel = vehicle.getFuel();
			int maxFuel = Vehicle.MAX_FUEL;
			float fuelPercentage = fuel / (float)maxFuel;
			float emptyPercentage = 1 - fuelPercentage;
			
			DrawingContext fuelViewContext = new TileDrawingContext(paddedTileContext, 10, 10, 8, 1, 1, 8);
			
			graphics.translate(fuelViewContext.x(), fuelViewContext.y());
			graphics.setColor(Color.gray);
			graphics.fillRect(0,  0, fuelViewContext.w(), fuelViewContext.h());
			graphics.setColor(Color.red);
			float emptyFuelBarY = fuelViewContext.h() * emptyPercentage;
			graphics.fillRect(0,  emptyFuelBarY, fuelViewContext.w(), fuelViewContext.h() - emptyFuelBarY);
			graphics.translate(-fuelViewContext.x(), -fuelViewContext.y());
			
		}
		
		
		if (item instanceof InsightInventoryItem) {
			
			DrawingContext numberViewContext = new TileDrawingContext(paddedTileContext, 2, 3, 1, 2);
			numberViewContext = new CenteredDrawingContext(numberViewContext, 3);

			TextBlock textBlock = new TextBlock();
			textBlock.setSpeaker(WritingRendererUtils.SPEAKER_SYSTEM);
			textBlock.setDisplayType(DynamicsTypes.STAMP.toString());
			textBlock.setText(String.valueOf(((InsightInventoryItem)item).getNumber()));
			textBlock.setEndsWithParagraph(true);
			
			String text = textBlock.getText();
			int textLength = text.length();
			
			for (int i = 0; i < textLength; i++) {
				Image letterImage = WritingRendererUtils.letterImageForCharacterAndTextBlock(text.charAt(i), textBlock);
				DrawingContext dc = new TileDrawingContext(numberViewContext, 2, 1, 2 - textLength + i, 0);
				
				graphics.setColor(Color.black);
				graphics.fillRect(dc.x(), dc.y(), dc.w(), dc.h());
				letterImage.draw(dc.x(), dc.y(), dc.w(), dc.h());
			}
			
		}
		
	}

}
