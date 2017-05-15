package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogState;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class GameLogRenderer implements Renderer {

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
			
//			graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.9f));
//			
//			graphics.fillRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());
//			
//			graphics.setColor(Color.black);
//			graphics.drawRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());

			ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.gamelogframe", mainDc);
			
			//DrawingContext authorDrawingContext = new TileDrawingContext(drawingContext, 1, 9, 0, 0);
			//DrawingContext textDrawingContext = new TileDrawingContext(drawingContext, 1, 9, 0, 1, 1, 7);
			
			DrawingContext headerDrawingContext = new SimpleDrawingContext(mainDc, 250, 30, 320, 25);
			DrawingContext textDrawingContext = new SimpleDrawingContext(mainDc, 35, 130, mainDc.w() - 70, mainDc.h() - 180);
			
//			graphics.setColor(Color.red);
//			graphics.fillRect(headerDrawingContext.x(), headerDrawingContext.y(), headerDrawingContext.w(), headerDrawingContext.h());
//			graphics.fillRect(textDrawingContext.x(), textDrawingContext.y(), textDrawingContext.w(), textDrawingContext.h());
			
			DrawingContext pressEnterDrawingContext = new TileDrawingContext(mainDc, 1, 4, 0, 3);
			
			textDrawingContext = new CenteredDrawingContext(textDrawingContext, 15); 
			
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, headerDrawingContext, gameLog.getHeader(), FontType.GameLogHeader, 0);
			TextBookRendererUtils.renderTextPage(gameContainer, graphics, textDrawingContext, gameLog.getLogText(), FontType.GameLog, page);
			
			boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
						
			if (renderBlinkingHint) {
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, pressEnterDrawingContext, "Press [ENTER]", FontType.PopUpInterface, 0);
			}
		}
		
		firstLoop = false;
	}

}
