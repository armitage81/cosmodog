package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogState;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class CutsceneRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	public static class GameLogRenderingParameter {
		public GameLog gameLog;
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		DrawingContext cutsceneTextDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneTextDrawingContext();
		DrawingContext cutsceneControlsDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneControlsDrawingContext();
		
		GameLogState openGameLog = ApplicationContextUtils.getCosmodogGame().getOpenGameLog();
		
		int page = openGameLog.getCurrentPage();
		GameLog gameLog = openGameLog.getGameLog();

		if (openGameLog != null) {
			
			
			graphics.setColor(Color.black);
			graphics.fillRect(
					gameContainerDrawingContext.x(),
					gameContainerDrawingContext.y(),
					gameContainerDrawingContext.w(),
					gameContainerDrawingContext.h());

			TextBookRendererUtils.renderTextPage(gameContainer, graphics, cutsceneTextDrawingContext, gameLog.getLogText(), FontType.GameLog, page);
			
			boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
						
			if (renderBlinkingHint) {
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, cutsceneControlsDrawingContext, "Press [ENTER]", FontType.PopUpInterface, 0);
			}
		}
		
		firstLoop = false;
	}

}
