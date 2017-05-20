package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.ingamemenu.InGameMenu;
import antonafanasjew.cosmodog.ingamemenu.InGameMenuFrame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class InGameMenuRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		InGameMenu inGameMenu = ApplicationContextUtils.getCosmodogGame().getInGameMenu();
		
		if (inGameMenu != null) {
			
			//Stop ambient sounds.
			ApplicationContextUtils.getCosmodogGame().getAmbientSoundRegistry().clear();
			
			InGameMenuFrame currentMenuFrame = inGameMenu.currentMenuFrame();
			
			ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.ingamemenuframe", drawingContext);
			
			
			DrawingContext headerDc = new SimpleDrawingContext(drawingContext, 33, 33, 1213, 61);
			DrawingContext contentDc = new SimpleDrawingContext(drawingContext, 33, 144, 1213, 431);
			DrawingContext footerDc = new SimpleDrawingContext(drawingContext, 33, 625, 1213, 61);
			
			String headerText = currentMenuFrame.getTitle();
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, headerDc, headerText, FontType.InGameMenuInterface, 0);
			
			String footerText = "[ESC] to close. [TAB] to switch.";
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, footerDc, footerText, FontType.InGameMenuInterface, 0);
			
			Renderer contentRenderer = currentMenuFrame.getContentRenderer();
			contentRenderer.render(gameContainer, graphics, contentDc, currentMenuFrame.getInputState());
			
		}
		
		firstLoop = false;
	}

}
