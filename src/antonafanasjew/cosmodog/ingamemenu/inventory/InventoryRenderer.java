package antonafanasjew.cosmodog.ingamemenu.inventory;

import java.util.Iterator;

import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.globals.ResolutionHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.notifications.NotificationQueue;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.DrawingContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.Mappings;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class InventoryRenderer extends AbstractRenderer {

	public static final int NUMBER_OF_VISIBLE_MESSAGES = NotificationQueue.MAX_MESSAGES_BEFORE_REMOVAL;
	public static final int INVENTORY_COLUMNS = 6;
	public static final int INVENTORY_ROWS = 5;

	
	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext inGameMenuContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().inGameMenuContentDrawingContext();
		
		InventoryInputState inventoryInputState = (InventoryInputState)renderingParameter;
		
		int selectionX = inventoryInputState.getSelectionX();
		int selectionY = inventoryInputState.getSelectionY();
		
		Cosmodog cosmodog = ApplicationContext.instance().getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
		Inventory inventory = player.getInventory();
		
		ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.ingameinventory", inGameMenuContentDrawingContext);
		
		DrawingContext itemsDrawingContext = itemsDrawingContext(inGameMenuContentDrawingContext);
		
				
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
				String animationId = Mappings.INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.get(item.getInventoryItemType());
				Animation itemAnimation = ApplicationContext.instance().getAnimations().get(animationId);
				
				DrawingContext picDc = new CenteredDrawingContext(tiledDrawingContext, 2);
				
				graphics.translate(picDc.x(), picDc.y());
				graphics.setColor(new Color(192,192,192));
				graphics.fillRect(0, 0, picDc.w(), picDc.h());
				itemAnimation.draw(0, 0, picDc.w(), picDc.h());
				graphics.translate(-picDc.x(), -picDc.y());
			}
			
			if (column == selectionX && row == selectionY) {
				selectedItem = item;
				ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.ingameinventoryitemboxselected", tiledDrawingContext);
			} else {
				ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.ingameinventoryitembox", tiledDrawingContext);
			}

		}
		
		DrawingContext descriptionDrawingContext = descriptionDrawingContext(inGameMenuContentDrawingContext);

		descriptionDrawingContext = new CenteredDrawingContext(descriptionDrawingContext, 20);

		String text = selectedItem == null ? "" : selectedItem.description();
		FontRefToFontTypeMap fontType = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.Informational);
		Book textBook = TextPageConstraints.fromDc(descriptionDrawingContext).textToBook(text, fontType);
		TextBookRendererUtils.renderDynamicTextPage(gameContainer, graphics, textBook);
		
				
	}
	
	private DrawingContext itemsDrawingContext(DrawingContext mainDc) {
		DrawingContext dc = new SimpleDrawingContext(null, 13 + 33, 13 + 144, 539, 406);
		dc = DrawingContextUtils.difResFromRef(dc, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		return dc;
	}
	
	private DrawingContext descriptionDrawingContext(DrawingContext mainDc) {
		DrawingContext dc = new SimpleDrawingContext(null, 584 + 33, 13 + 144, 617, 406);
		dc = DrawingContextUtils.difResFromRef(dc, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		return dc;
	}

}
