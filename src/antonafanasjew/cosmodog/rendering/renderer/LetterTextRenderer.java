package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterUtils;

public class LetterTextRenderer implements Renderer {

	private static LetterTextRenderer instance = new LetterTextRenderer();
	
	public static LetterTextRenderer getInstance() {
		return instance;
	}
	
	public static class LetterTextRenderingParameter {
		
		public static LetterTextRenderingParameter fromText(String text) {
			return fromTextAndScaleFactor(text, 1.0f);
		}
		
		public static LetterTextRenderingParameter fromTextAndScaleFactor(String text, float scaleFactor) {
			LetterTextRenderingParameter instance = new LetterTextRenderingParameter();
			instance.text = text;
			instance.scaleFactor = scaleFactor;
			return instance;
		}
		
		public String text;
		public float scaleFactor;
	}
	

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		LetterTextRenderingParameter param = (LetterTextRenderingParameter)renderingParameter;
		
		String text = param.text;
		float letterScaleFactor = param.scaleFactor; 

		List<Letter> letters = LetterUtils.lettersForText(text, ApplicationContext.instance().getCharacterLetters(), ApplicationContext.instance().getCharacterLetters().get('?'));
		
		
		List<DrawingContext> letterDrawingContexts = LetterUtils.letterLineDrawingContexts(letters, 0, letterScaleFactor, drawingContext);
		
		for (int i = 0; i < letters.size(); i++) {
			DrawingContext dc = letterDrawingContexts.get(i);
			Letter l = letters.get(i);
			graphics.drawImage(l.getImage().getScaledCopy(letterScaleFactor), dc.x(), dc.y());
			
		}
		
	}

}
