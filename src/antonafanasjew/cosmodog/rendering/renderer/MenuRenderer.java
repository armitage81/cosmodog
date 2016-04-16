package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.model.menu.Menu;
import antonafanasjew.cosmodog.model.menu.MenuElement;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;

public class MenuRenderer implements Renderer {

	public static class MenuRenderingParam {
		public Menu menu;
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics g, DrawingContext drawingContext, Object renderingParameter) {
		
		Menu menu = ((MenuRenderingParam)renderingParameter).menu;
		
		
		DrawingContext dcCursor = new TileDrawingContext(drawingContext, 15, 1, 0, 0, 1, 1);
		DrawingContext dcMenu = new TileDrawingContext(drawingContext, 15, 1, 1, 0, 14, 1);
		
		int numberOfElements = menu.getMenuElements().size();
		
		g.setColor(Color.white);
		
		for (int i = 0; i < numberOfElements; i++) {
			
			DrawingContext itemDc = new TileDrawingContext(dcMenu, 1, numberOfElements, 0, i);
			
			MenuElement menuElement = menu.getMenuElements().get(i);
			
			LetterTextRenderer.getInstance().render(gameContainer, g, itemDc, LetterTextRenderingParameter.fromTextAndScaleFactor(menuElement.getLabel(), 2f));
			
			if (menu.getSelectedMenuElement().equals(menuElement)) {
				DrawingContext cursorDc = new TileDrawingContext(dcCursor, 1, numberOfElements, 0, i);
				cursorDc = new CenteredDrawingContext(cursorDc, 30, 30);
				
				g.translate(cursorDc.x(), cursorDc.y());
				
				g.setColor(Color.black);
				
				g.fillRect(0, 0, 10, 10);
				
				g.setColor(Color.white);
				g.translate(-cursorDc.x(), -cursorDc.y());
			}
			
		}
		
	}

}
