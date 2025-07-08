package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;
import antonafanasjew.cosmodog.writing.textframe.TextFrame;

public class TextFrameRenderer extends AbstractRenderer {

	public static boolean firstLoop = true;
	

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		long referenceTime = System.currentTimeMillis();
		
		DrawingContext textFrameDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().textFrameDrawingContext();
		DrawingContext textFrameHeaderDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().textFrameHeaderDrawingContext();
		DrawingContext textFrameControlsDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().textFrameControlsDrawingContext();
		
		TextFrame textFrame = ApplicationContextUtils.getCosmodogGame().getTextFrame();
		
		if (textFrame != null) {
			
			FontRefToFontTypeMap fontTypeHeader = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.MainHeader);
			FontRefToFontTypeMap fontTypeControlsHint = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
			
			graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.75f));
			
			ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.popupframe", textFrameDrawingContext);
			
			TextBookRendererUtils.renderFromParam(gameContainer, graphics, renderingParameter);
			
			Book textBook = TextPageConstraints.fromDc(textFrameHeaderDrawingContext).textToBook("Message", fontTypeHeader);
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook);
			
			boolean renderBlinkingHint = (referenceTime / 250 % 2) == 1;
			if (renderBlinkingHint) {
				Book controlHint = TextPageConstraints.fromDc(textFrameControlsDrawingContext).textToBook("Press [ENTER]", fontTypeControlsHint);
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, controlHint);
			}
		}
		
		firstLoop = false;
	}

}
