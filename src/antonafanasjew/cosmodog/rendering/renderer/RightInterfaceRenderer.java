package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
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

public class RightInterfaceRenderer implements Renderer {

	public static final int NUMBER_OF_VISIBLE_MESSAGES = NotificationQueue.MAX_MESSAGES_BEFORE_REMOVAL;
	private static final int INVENTORY_COLUMNS = 3;
	private static final int INVENTORY_ROWS = 10;
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext context, Object renderingParameter) {
		
		graphics.translate(context.x(), context.y());
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, context.w(), context.h());
		graphics.translate(-context.x(), -context.y());
		
		Cosmodog cosmodog = ApplicationContext.instance().getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
		Inventory inventory = player.getInventory();
		
		DrawingContext contentContext = new CenteredDrawingContext(context, context.w() - 10, context.h() - 10);
		
		graphics.translate(contentContext.x(), contentContext.y());
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, contentContext.w(), contentContext.h());
		graphics.translate(-contentContext.x(), -contentContext.y());
		
		int i = 0;
		for (InventoryItemType key : inventory.keySet()) {
			int column = i % INVENTORY_COLUMNS;
			int row = i / INVENTORY_COLUMNS;
			InventoryItem item = inventory.get(key);
			DrawingContext tiledDrawingContext = new TileDrawingContext(contentContext, INVENTORY_COLUMNS, INVENTORY_ROWS, column, row);
			InventoryItemRenderer inventoryItemRenderer = new InventoryItemRenderer();
			inventoryItemRenderer.render(gameContainer, graphics, tiledDrawingContext, item);

			i++;
		}
		
		

			
	}

}
