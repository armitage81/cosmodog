package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class GameLogRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	public static class GameLogRenderingParameter {
		public GameLog gameLog;
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

		GameLog openGameLog = ApplicationContextUtils.getCosmodogGame().getOpenGameLog();
		
		if (openGameLog != null) {
			
			graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.9f));
			
			graphics.fillRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());
			
			graphics.setColor(Color.black);
			graphics.drawRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());

			DrawingContext authorDrawingContext = new TileDrawingContext(drawingContext, 1, 7, 0, 0);
			DrawingContext textDrawingContext = new TileDrawingContext(drawingContext, 1, 7, 0, 1, 1, 5);
			DrawingContext pressEnterDrawingContext = new TileDrawingContext(drawingContext, 1, 7, 0, 6, 1, 1);
			
			authorDrawingContext = new CenteredDrawingContext(authorDrawingContext, 15);
			textDrawingContext = new CenteredDrawingContext(textDrawingContext, 15); 
			
			TextBookRendererUtils.renderTextPage(gameContainer, graphics, authorDrawingContext, openGameLog.getHeader(), FontType.GameLogHeader);
			TextBookRendererUtils.renderTextPage(gameContainer, graphics, textDrawingContext, openGameLog.getLogText(), FontType.GameLog);
			
			boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
			if (renderBlinkingHint) {
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, pressEnterDrawingContext, "Press [ENTER]", FontType.PopUpInterface);
			}
		}
		
		firstLoop = false;
	}

}
