package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.writing.textframe.TextFrame;

public class TextFrameRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		TextFrame textFrame = ApplicationContextUtils.getCosmodogGame().getTextFrame();
		
		if (textFrame != null) {
			
			graphics.translate(drawingContext.x() + drawingContext.w() / 2, drawingContext.y() + drawingContext.h() / 2);
			graphics.scale(2, 2);
			graphics.translate(-drawingContext.x() - drawingContext.w() / 2, -drawingContext.y() - drawingContext.h() / 2);
			
			graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.75f));
			
			graphics.fillRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());
			
			graphics.setColor(Color.black);
			
			graphics.drawRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());

			
			
			List<DrawingContext> drawingContexts = textFrame.getLetterDrawingContexts(drawingContext);
			List<Letter> letters = textFrame.getLetters(drawingContext);
			
			for (int i = 0; i < letters.size(); i++) {
				DrawingContext dc = drawingContexts.get(i);
				Letter l = letters.get(i);
				graphics.drawImage(l.getImage(), dc.x(), dc.y());
				
			}
			
		}
		
		firstLoop = false;
	}

}
