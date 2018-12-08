package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.ingamemenu.InGameMenu;
import antonafanasjew.cosmodog.ingamemenu.InGameMenuFrame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class InGameMenuRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		InGameMenu inGameMenu = ApplicationContextUtils.getCosmodogGame().getInGameMenu();
		
		if (inGameMenu != null) {

			DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
			DrawingContext inGameMenuHeaderDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().inGameMenuHeaderDrawingContext();
			DrawingContext inGameMenuFooterDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().inGameMenuFooterDrawingContext();
			
			//Stop ambient sounds.
			ApplicationContextUtils.getCosmodogGame().getAmbientSoundRegistry().clear();
			
			InGameMenuFrame currentMenuFrame = inGameMenu.currentMenuFrame();
						
			ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.ingamemenuframe", gameContainerDrawingContext);
			
			String headerText = currentMenuFrame.getTitle();
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, inGameMenuHeaderDrawingContext, headerText, FontType.InGameMenuInterface, 0);
			
			int noOfFrames = inGameMenu.getMenuFrames().size();
			
			String footerText = noOfFrames > 1 ? "[ESC] to close. [TAB] to switch." : "[ESC] to close.";
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, inGameMenuFooterDrawingContext, footerText, FontType.InGameMenuInterface, 0);
			
			Renderer contentRenderer = currentMenuFrame.getContentRenderer();
			contentRenderer.render(gameContainer, graphics, currentMenuFrame.getInputState());
			
		}
		
		firstLoop = false;
	}

}
