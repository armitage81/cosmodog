package antonafanasjew.cosmodog.ingamemenu.options;



import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterUtils;
import antonafanasjew.cosmodog.topology.Rectangle;

public class OptionsRenderer implements Renderer {

	private static final String TEXT = "Press [RETURN] to save and quit";
	private static final float FONT_SCALE = 2.0f;

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		ApplicationContext appCx = ApplicationContext.instance();
		List<Letter> letters = LetterUtils.lettersForText(TEXT, appCx.getCharacterLetters(), appCx.getCharacterLetters().get('?'));
		Rectangle r = LetterUtils.letterListBounds(letters, 0);
		DrawingContext lineDc = new CenteredDrawingContext(drawingContext, r.getWidth() * FONT_SCALE, r.getHeight() * FONT_SCALE);
		LetterTextRenderer.getInstance().render(gameContainer, graphics, lineDc, LetterTextRenderingParameter.fromTextAndScaleFactor(TEXT, FONT_SCALE));
	}

}
