package antonafanasjew.cosmodog.ingamemenu.inventory;

import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.notifications.NotificationQueue;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.InventoryItemRenderer;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class InventoryRenderer implements Renderer {

	public static final int NUMBER_OF_VISIBLE_MESSAGES = NotificationQueue.MAX_MESSAGES_BEFORE_REMOVAL;
	public static final int INVENTORY_COLUMNS = 10;
	public static final int INVENTORY_ROWS = 8;

	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext context, Object renderingParameter) {
		
		InventoryInputState inventoryInputState = (InventoryInputState)renderingParameter;
		
		int selectionX = inventoryInputState.getSelectionX();
		int selectionY = inventoryInputState.getSelectionY();
		
		Cosmodog cosmodog = ApplicationContext.instance().getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
		Inventory inventory = player.getInventory();
		
		DrawingContext contentContext = new CenteredDrawingContext(context, context.w() - 10, context.h() - 10);
		
		DrawingContext itemsDrawingContext = itemsDrawingContext(contentContext);
		
		graphics.translate(itemsDrawingContext.x(), itemsDrawingContext.y());
		graphics.setColor(Color.white);
		graphics.drawRoundRect(0, 0, itemsDrawingContext.w(), itemsDrawingContext.h(), 5);
		graphics.translate(-itemsDrawingContext.x(), -itemsDrawingContext.y());
		
				
		Iterator<InventoryItemType> itemTypeIterator = inventory.keySet().iterator();
		
		InventoryItem selectedItem = null;
		
		for (int i = 0; i < (INVENTORY_COLUMNS * INVENTORY_ROWS); i++) {
			int column = i % INVENTORY_COLUMNS;
			int row = i / INVENTORY_COLUMNS;

			InventoryItem item = null;
			if (itemTypeIterator.hasNext()) {
				InventoryItemType nextItemType = null;
				nextItemType = itemTypeIterator.next();
				item = inventory.get(nextItemType);
			}
			
			DrawingContext tiledDrawingContext = new TileDrawingContext(itemsDrawingContext, INVENTORY_COLUMNS, INVENTORY_ROWS, column, row);
			
			if (item != null) {
				InventoryItemRenderer inventoryItemRenderer = new InventoryItemRenderer();
				inventoryItemRenderer.render(gameContainer, graphics, tiledDrawingContext, item);
			}
			
			if (column == selectionX && row == selectionY) {
				selectedItem = item;
				graphics.setColor(Color.white);
				graphics.drawRect(tiledDrawingContext.x(), tiledDrawingContext.y(), tiledDrawingContext.w(), tiledDrawingContext.h());
			}

		}
		
		DrawingContext descriptionDrawingContext = descriptionDrawingContext(contentContext);
		
		graphics.translate(descriptionDrawingContext.x(), descriptionDrawingContext.y());
		graphics.setColor(Color.white);
		graphics.drawRoundRect(0, 0, descriptionDrawingContext.w(), descriptionDrawingContext.h(), 5);
		graphics.translate(-descriptionDrawingContext.x(), -descriptionDrawingContext.y());

		descriptionDrawingContext = new CenteredDrawingContext(descriptionDrawingContext, 20);
		
		
		String text = selectedItem == null ? "" : selectedItem.description();
		TextBookRendererUtils.renderTextPage(gameContainer, graphics, descriptionDrawingContext, text, FontType.InventoryDescription);
		
				
	}
	
	private DrawingContext itemsDrawingContext(DrawingContext mainDc) {
		return new TileDrawingContext(mainDc, 3, 1, 0, 0, 2, 1);
	}
	
	private DrawingContext descriptionDrawingContext(DrawingContext mainDc) {
		return new TileDrawingContext(mainDc, 3, 1, 2, 0, 1, 1);
	}

}
