package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogState;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class CutsceneRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	public static class GameLogRenderingParameter {
		public GameLog gameLog;
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext dc, Object renderingParameter) {

		DrawingContext mainDc = dc;
		
		GameLogState openGameLog = ApplicationContextUtils.getCosmodogGame().getOpenGameLog();
		
		int page = openGameLog.getCurrentPage();
		GameLog gameLog = openGameLog.getGameLog();

		if (openGameLog != null) {
			
			
			graphics.setColor(Color.black);
			graphics.fillRect(0, 0, gameContainer.getWidth(), gameContainer.getHeight());
			
//			graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.9f));
//			
//			graphics.fillRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());
//			
//			graphics.setColor(Color.red);
//			graphics.drawRect(mainDc.x(), mainDc.y(), mainDc.w(), mainDc.h());

//			ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.gamelogframe", mainDc);
			
			//DrawingContext authorDrawingContext = new TileDrawingContext(drawingContext, 1, 9, 0, 0);
			//DrawingContext textDrawingContext = new TileDrawingContext(drawingContext, 1, 9, 0, 1, 1, 7);
			
//			DrawingContext headerDrawingContext = new SimpleDrawingContext(mainDc, 25, 14, Constants.GAME_LOG_TEXT_WIDTH, 34);
			DrawingContext textDrawingContext = new SimpleDrawingContext(mainDc, 25, 60, Constants.GAME_LOG_TEXT_WIDTH, Constants.GAME_LOG_TEXT_HEIGHT);
			
			DrawingContext pressEnterDrawingContext = new SimpleDrawingContext(mainDc, 25, 525, Constants.GAME_LOG_TEXT_WIDTH, 35);

//			graphics.setColor(Color.red);
//			graphics.drawRect(headerDrawingContext.x(), headerDrawingContext.y(), headerDrawingContext.w(), headerDrawingContext.h());
//			graphics.drawRect(textDrawingContext.x(), textDrawingContext.y(), textDrawingContext.w(), textDrawingContext.h());
//			graphics.drawRect(pressEnterDrawingContext.x(), pressEnterDrawingContext.y(), pressEnterDrawingContext.w(), pressEnterDrawingContext.h());
			
//			graphics.drawRect(textDrawingContext.x(), textDrawingContext.y(), textDrawingContext.w(), textDrawingContext.h());
			
//			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, headerDrawingContext, gameLog.getHeader(), FontType.GameLogHeader, 0);
			TextBookRendererUtils.renderTextPage(gameContainer, graphics, textDrawingContext, gameLog.getLogText(), FontType.GameLog, page);
			
			boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
						
			if (renderBlinkingHint) {
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, pressEnterDrawingContext, "Press [ENTER]", FontType.PopUpInterface, 0);
			}
		}
		
		firstLoop = false;
	}

}
