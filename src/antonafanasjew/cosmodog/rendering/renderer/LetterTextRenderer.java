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
			LetterTextRenderingParameter instance = new LetterTextRenderingParameter();
			instance.text = text;
			return instance;
		}
		
		public String text;
	}
	

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		LetterTextRenderingParameter param = (LetterTextRenderingParameter)renderingParameter;
		
		String text = param.text;
		List<Letter> letters = LetterUtils.lettersForText(text, ApplicationContext.instance().getCharacterLetters(), ApplicationContext.instance().getCharacterLetters().get('?'));
		List<DrawingContext> letterDrawingContexts = LetterUtils.letterLineDrawingContexts(letters, 0, drawingContext);
		
		for (int i = 0; i < letters.size(); i++) {
			DrawingContext dc = letterDrawingContexts.get(i);
			Letter l = letters.get(i);
			graphics.drawImage(l.getImage(), dc.x(), dc.y());
			
		}
		
	}

}
