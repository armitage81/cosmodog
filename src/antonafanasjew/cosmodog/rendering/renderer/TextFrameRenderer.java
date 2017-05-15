package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;
import antonafanasjew.cosmodog.writing.textframe.TextFrame;

public class TextFrameRenderer implements Renderer {

	public static boolean firstLoop = true;
	

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		TextFrame textFrame = ApplicationContextUtils.getCosmodogGame().getTextFrame();
		
		if (textFrame != null) {
			
			graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.75f));
			
			ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.popupframe", drawingContext);
			
//			graphics.fillRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());
//			
//			graphics.setColor(Color.black);
//			graphics.drawRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());

			DrawingContext headerDrawingContext = new SimpleDrawingContext(drawingContext, 130, 10, 250, 60);
			DrawingContext textDrawingContext = new SimpleDrawingContext(drawingContext, 50, 100, 400, 200);
			
			
			DrawingContext pressEnterDrawingContext = new TileDrawingContext(drawingContext, 1, 3, 0, 2);
			
			
			textDrawingContext = new CenteredDrawingContext(textDrawingContext, 30); 
			
			TextBookRendererUtils.renderFromParam(gameContainer, graphics, textDrawingContext, renderingParameter);
			
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, headerDrawingContext, "Message", FontType.PopUpHeader, 0);
			
			boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
			if (renderBlinkingHint) {
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, pressEnterDrawingContext, "Press [ENTER]", FontType.PopUpInterface, 0);
			}
		}
		
		firstLoop = false;
	}

}
