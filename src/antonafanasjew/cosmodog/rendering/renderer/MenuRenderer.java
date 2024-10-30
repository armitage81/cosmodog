package antonafanasjew.cosmodog.rendering.renderer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.Log;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.menu.Menu;
import antonafanasjew.cosmodog.model.menu.MenuElement;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

/**
 *
 * This renderer uses a cache to hold the menu labels. Reason is: Some labels are based on the game state so they are
 * calculated by loading the game save header. It would be too costly to do it every time, hence the cache.
 * The cache has to be reset every time a game state changes. It is enough to reset the cache every time the menu game state is entered.
 */
public class MenuRenderer implements Renderer {

	public static final int MAX_MENU_ENTRIES_RENDERED = 5;
	
	public static class MenuRenderingParam {
		public Menu menu;
	}
	
	private final Cache<MenuElement, String> menuLabelCache = CacheBuilder.newBuilder().maximumSize(100).build();
	
	public void resetMenuLabelCache() {
		menuLabelCache.invalidateAll();
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics g, Object renderingParameter) {
		
		
		DrawingContext startScreenMenuDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().startScreenMenuDrawingContext();

		Menu menu = ((MenuRenderingParam)renderingParameter).menu;
		
		DrawingContext dcCursor = new TileDrawingContext(startScreenMenuDrawingContext, 15, 1, 0, 0, 1, 1);
		DrawingContext dcMenu = new TileDrawingContext(startScreenMenuDrawingContext, 15, 1, 1, 0, 14, 1);
		
		int numberOfElements = menu.getMenuElements().size();
		
		g.setColor(Color.white);
		
		for (int i = 0; i < MAX_MENU_ENTRIES_RENDERED; i++) {
			
			if (i < numberOfElements) {
				DrawingContext itemDc = new TileDrawingContext(dcMenu, 1, MAX_MENU_ENTRIES_RENDERED, 0, i);
						
				MenuElement menuElement = menu.getMenuElements().get(i);

				String labelText;
				
				try {
					labelText = menuLabelCache.get(menuElement, new Callable<String>() {
						@Override
						public String call() throws Exception {
							return menuElement.getLabel().labelText();
						}
					});
				} catch (ExecutionException e) {
					labelText = "<ERROR>";
					Log.error(e.getLocalizedMessage(), e);
				}
				
				boolean renderSelected = true;
				
				if (menu.getSelectedMenuElement().equals(menuElement)) {
					
					renderSelected = System.currentTimeMillis() / 100 % 2 == 0;
					
					DrawingContext cursorDc = new TileDrawingContext(dcCursor, 1, MAX_MENU_ENTRIES_RENDERED, 0, i);
					cursorDc = new CenteredDrawingContext(cursorDc, 10, 10);
					
					g.translate(cursorDc.x(), cursorDc.y());
					
					g.setColor(Color.black);
					g.setLineWidth(3);
					
					g.drawRect(0, 0, 10, 10);
					
					g.setColor(Color.white);
					g.translate(-cursorDc.x(), -cursorDc.y());
				}
				
				if (renderSelected) {
					FontRefToFontTypeMap fontType = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.MainMenu);
					Book textBook = TextPageConstraints.fromDc(itemDc).textToBook(labelText, fontType);
					TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, g, textBook);
				}
			}
			
		}
		
	}

}
