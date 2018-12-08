package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogState;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class GameLogRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	public static class GameLogRenderingParameter {
		public GameLog gameLog;
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		DrawingContext gameLogDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameLogDrawingContext();
		DrawingContext gameLogHeaderDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameLogHeaderDrawingContext();
		DrawingContext gameLogContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameLogContentDrawingContext();
		DrawingContext gameLogControlsDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameLogControlsDrawingContext();
		
		GameLogState openGameLog = ApplicationContextUtils.getCosmodogGame().getOpenGameLog();
		
		int page = openGameLog.getCurrentPage();
		GameLog gameLog = openGameLog.getGameLog();

		if (openGameLog != null) {
			
			ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.gamelogframe", gameLogDrawingContext);
			
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, gameLogHeaderDrawingContext, gameLog.getHeader(), FontType.GameLogHeader, 0);
			TextBookRendererUtils.renderTextPage(gameContainer, graphics, gameLogContentDrawingContext, gameLog.getLogText(), FontType.GameLog, page);
			
			boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
						
			if (renderBlinkingHint) {
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, gameLogControlsDrawingContext, "Press [ENTER]", FontType.PopUpInterface, 0);
			}
		}
		
		firstLoop = false;
	}

}
