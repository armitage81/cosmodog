package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterUtils;
import antonafanasjew.cosmodog.topology.Rectangle;

public class LetterTextRenderer implements Renderer {

	private static LetterTextRenderer instance = new LetterTextRenderer();
	
	public static LetterTextRenderer getInstance() {
		return instance;
	}
	
	public static class LetterTextRenderingParameter {
		
		public static final short HOR_ALIGNMENT_LEFT = 0;
		public static final short HOR_ALIGNMENT_CENTER = 1;
		public static final short HOR_ALIGNMENT_RIGHT = 2;
		
		public static final short VER_ALIGNMENT_TOP = 0;
		public static final short VER_ALIGNMENT_CENTER = 1;
		public static final short VER_ALIGNMENT_BOTTOM = 2;
		
		public static LetterTextRenderingParameter fromText(String text) {
			return fromTextAndScaleFactor(text, 1.0f);
		}
		
		public static LetterTextRenderingParameter fromTextAndScaleFactor(String text, float scaleFactor) {
			return fromTextScaleFactorAndAlignment(text, scaleFactor, HOR_ALIGNMENT_LEFT, VER_ALIGNMENT_TOP);
		}
		
		public static LetterTextRenderingParameter fromTextScaleFactorAndAlignment(String text, float scaleFactor, short horAlignment, short verAlignment) {
			LetterTextRenderingParameter instance = new LetterTextRenderingParameter();
			instance.text = text;
			instance.scaleFactor = scaleFactor;
			instance.horAlignment = horAlignment;
			instance.verAlignment = verAlignment;
			return instance;
		}
		
		public String text;
		public float scaleFactor;
		public short horAlignment;
		public short verAlignment;
	}
	

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		LetterTextRenderingParameter param = (LetterTextRenderingParameter)renderingParameter;
		
		String text = param.text;
		float letterScaleFactor = param.scaleFactor; 
		short horAlignment = param.horAlignment;
		short verAlignment = param.verAlignment;

		List<Letter> letters = LetterUtils.lettersForText(text, ApplicationContext.instance().getCharacterLetters(), ApplicationContext.instance().getCharacterLetters().get('?'));
		
		
		List<DrawingContext> letterDrawingContexts = LetterUtils.letterLineDrawingContexts(letters, 0, letterScaleFactor, drawingContext);
		Rectangle textBounds = LetterUtils.letterListBounds(letters, 0);
		float textWidth = textBounds.getWidth() * letterScaleFactor;
		float textHeight = textBounds.getHeight() * letterScaleFactor;
		
		for (int i = 0; i < letters.size(); i++) {
			DrawingContext dc = letterDrawingContexts.get(i);
			Letter l = letters.get(i);
			
			
			float offsetX = 0;
			float offsetY = 0;
			
			if (horAlignment == LetterTextRenderingParameter.HOR_ALIGNMENT_CENTER) {
				offsetX = (drawingContext.w() - textWidth) / 2;
			} else if (horAlignment == LetterTextRenderingParameter.HOR_ALIGNMENT_RIGHT) {
				offsetX = drawingContext.w() - textWidth;
			}
			
			if (verAlignment == LetterTextRenderingParameter.VER_ALIGNMENT_CENTER) {
				offsetY = (drawingContext.h() - textHeight) / 2;
			} else if (verAlignment == LetterTextRenderingParameter.VER_ALIGNMENT_BOTTOM) {
				offsetY = drawingContext.h() - textHeight;
			}
			
			graphics.translate(offsetX, offsetY);
			graphics.drawImage(l.getImage().getScaledCopy(letterScaleFactor), dc.x(), dc.y());
			graphics.translate(-offsetX, -offsetY);
			
			
			
		}
		
	}

}
