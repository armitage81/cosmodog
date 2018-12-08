package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;
import antonafanasjew.cosmodog.writing.textframe.TextFrame;

public class TextFrameRenderer implements Renderer {

	public static boolean firstLoop = true;
	

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext textFrameDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().textFrameDrawingContext();
		DrawingContext textFrameHeaderDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().textFrameHeaderDrawingContext();
		DrawingContext textFrameContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().textFrameContentDrawingContext();
		DrawingContext textFrameControlsDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().textFrameControlsDrawingContext();
		
		TextFrame textFrame = ApplicationContextUtils.getCosmodogGame().getTextFrame();
		
		if (textFrame != null) {
			
			graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.75f));
			
			ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.popupframe", textFrameDrawingContext);
			
			TextBookRendererUtils.renderFromParam(gameContainer, graphics, textFrameContentDrawingContext, renderingParameter);
			
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textFrameHeaderDrawingContext, "Message", FontType.PopUpHeader, 0);
			
			boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
			if (renderBlinkingHint) {
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textFrameControlsDrawingContext, "Press [ENTER]", FontType.PopUpInterface, 0);
			}
		}
		
		firstLoop = false;
	}

}
