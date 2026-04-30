package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterUtils;

public class OverheadPixelTextRenderer extends AbstractRenderer {

	private static final OverheadPixelTextRenderer instance = new OverheadPixelTextRenderer();

	public static OverheadPixelTextRenderer instance() {
		return instance;
	}

	private OverheadPixelTextRenderer() {
	}

	public static class OverheadPixelTextParam {
		public String text;
		public Color color;
		public Color outlineColor;
		public float scaleFactor;
		public float x;
		public float y;
		public float maxWidth;
	}

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		OverheadPixelTextParam param = (OverheadPixelTextParam) renderingParameter;

		Map<Character, Letter> letters = ApplicationContext.instance().getOverheadCharacterLetters();
		Letter defaultLetter = letters.get('?');

		// The visible outline strip = radius - strokeWidth/2. To match pen strike (~scaleFactor px)
		// the radius must be strokeWidth/2 + scaleFactor ≈ scaleFactor * 2.
		int outlineRadius = Math.round(param.scaleFactor * 2);

		List<String> lines = LetterUtils.splitTextForTextBoxWidth(param.text, param.scaleFactor, param.maxWidth);

		float lineY = param.y;

		for (String line : lines) {
			List<Letter> lineLetters = LetterUtils.lettersForText(line.trim(), letters, defaultLetter);
			DrawingContext parentDc = new SimpleDrawingContext(null, param.x, lineY, param.maxWidth, Letter.LETTER_HEIGHT * param.scaleFactor);
			List<DrawingContext> drawingContexts = LetterUtils.letterLineDrawingContexts(lineLetters, 0, param.scaleFactor, parentDc);

			for (int i = 0; i < lineLetters.size(); i++) {
				Letter letter = lineLetters.get(i);
				DrawingContext dc = drawingContexts.get(i);
				Image scaled = letter.getImage().getScaledCopy(param.scaleFactor);
				float px = Math.round(dc.x());
				float py = Math.round(dc.y());

				for (int dx = -outlineRadius; dx <= outlineRadius; dx++) {
					for (int dy = -outlineRadius; dy <= outlineRadius; dy++) {
						if (dx != 0 || dy != 0) {
							scaled.draw(px + dx, py + dy, param.outlineColor);
						}
					}
				}
				scaled.draw(px, py, param.color);
			}

			lineY += Letter.LETTER_HEIGHT * param.scaleFactor;
		}
	}

}
