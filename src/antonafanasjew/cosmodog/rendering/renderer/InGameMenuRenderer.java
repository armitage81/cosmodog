package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.ingamemenu.InGameMenu;
import antonafanasjew.cosmodog.ingamemenu.InGameMenuFrame;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer.TextBookRendererParameter;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class InGameMenuRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		InGameMenu inGameMenu = ApplicationContextUtils.getCosmodogGame().getInGameMenu();
		
		if (inGameMenu != null) {
			
			InGameMenuFrame currentMenuFrame = inGameMenu.currentMenuFrame();
			
			graphics.setColor(new Color(0.0f, 0.0f, 0.0f));
			
			graphics.fillRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());
			
			graphics.setColor(Color.black);
			graphics.drawRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());

			
			DrawingContext headerDc = new TileDrawingContext(drawingContext, 1, 10, 0, 0);
			headerDc = new CenteredDrawingContext(headerDc, 5);
			DrawingContext contentDc = new TileDrawingContext(drawingContext, 1, 10, 0, 1, 1, 8);
			contentDc = new CenteredDrawingContext(contentDc, 5);
			DrawingContext footerDc = new TileDrawingContext(drawingContext, 1, 10, 0, 9);
			footerDc = new CenteredDrawingContext(footerDc, 5);
			
			graphics.setColor(Color.white);
			graphics.drawRoundRect(headerDc.x(), headerDc.y(), headerDc.w(), headerDc.h(), 5);
			
			graphics.drawRoundRect(contentDc.x(), contentDc.y(), contentDc.w(), contentDc.h(), 5);
			
			graphics.drawRoundRect(footerDc.x(), footerDc.y(), footerDc.w(), footerDc.h(), 5);
			
			String headerText = currentMenuFrame.getTitle();
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, headerDc, headerText, FontType.InGameMenuInterface);
			
			String footerText = "[ESC] to close. [TAB] to switch.";
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, footerDc, footerText, FontType.InGameMenuInterface);
			
			Renderer contentRenderer = currentMenuFrame.getContentRenderer();
			contentRenderer.render(gameContainer, graphics, contentDc, currentMenuFrame.getInputState());
			
		}
		
		firstLoop = false;
	}

}
