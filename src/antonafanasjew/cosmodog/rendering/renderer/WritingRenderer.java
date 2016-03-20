package antonafanasjew.cosmodog.rendering.renderer;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.util.WritingRendererUtils;
import antonafanasjew.cosmodog.writing.model.TextBlock;
import antonafanasjew.cosmodog.writing.model.TextBlockBox;
import antonafanasjew.cosmodog.writing.model.TextBlockLine;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBox;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxState;

/**
 * This renderer is not responsible for calculating if given words fit in
 * the drawing context or in each line. The caller needs to make sure, the bounds are correct
 * otherwise the rendering will be undefined.
 */
public class WritingRenderer implements Renderer {

	private boolean centered = false;
	private Color background = null;
	
	public WritingRenderer() {
		this(false);
	}
	
	
	public WritingRenderer(boolean centered) {
		this(centered, null);
	}
	
	public WritingRenderer(boolean centered, Color background) {
		this.centered = centered;
		this.background  = background;
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		WritingTextBoxState param = (WritingTextBoxState)renderingParameter;
		WritingTextBox textBox = param.getWritingTextBoxContent().getWritingTextBox();
		
		TextBlockBox box = param.dynamicTextOfCurrentBox();
		
		
		for (int lineIndex = 0; lineIndex < box.size(); lineIndex++) {
			TextBlockLine line = box.get(lineIndex);
			for (int charIndex = 0; charIndex < line.aggregatedText().length(); charIndex++) {
				
				TextBlock textBlock = line.textBlockForCharacterIndex(charIndex);
				char character = line.aggregatedText().charAt(charIndex);

				int x;
				
				if (centered) {
					x = (int)textBox.xForLetterCentered(charIndex, line.aggregatedText().length());
				} else {
					x = (int)textBox.xForLetter(charIndex);
				}
				
				
				int y = (int)textBox.yForLine(lineIndex);
				
				Image characterImage = WritingRendererUtils.letterImageForCharacterAndTextBlock(character, textBlock);

				SimpleDrawingContext characterDc = new SimpleDrawingContext(drawingContext, x, y, textBox.getLetterWidth(), textBox.getLetterHeight());
				graphics.translate(characterDc.x(), characterDc.y());
				
				if (background != null) {
					graphics.setColor(background);
					graphics.fillRect(0, 0, textBox.getLetterWidth(), textBox.getLetterHeight());
				}
				characterImage.draw(0, 0, textBox.getLetterWidth(), textBox.getLetterHeight());
				graphics.translate(-characterDc.x(), -characterDc.y());
			}
			
			
		}
		
	}

}
