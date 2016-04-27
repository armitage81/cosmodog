package antonafanasjew.cosmodog.rendering.renderer;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class TextRenderer extends AbstractRenderer {

	public static final TrueTypeFont FONT = new TrueTypeFont(new Font("Verdana", Font.BOLD, 12), true);
	private boolean centered;
	private TrueTypeFont trueTypeFont;
	private float textWidth;
	private float textHeight;
	private Color textColor; 
	private Color backgroundColor;
	
	public TextRenderer(TrueTypeFont font, boolean centered, Color textColor, Color backgroundColor) {
		this.centered = centered;
		this.trueTypeFont = font;
		this.textHeight = trueTypeFont.getHeight();
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
	}

	public TextRenderer(TrueTypeFont font, boolean centered) {
		this(font, centered, Color.white, Color.black);
	}
	
	public TextRenderer() {
		this(FONT, false);
	}

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext context, Object renderingParameter) {
		
		String text = (String)renderingParameter;
		this.textWidth = trueTypeFont.getWidth(text);
		
		float offsetX = 0;
		float offsetY = 0;

		if (centered) {

			offsetX = (context.w() - textWidth) / 2.0f;
			offsetY = (context.h() - textHeight) / 2.0f;
		}
		graphics.setColor(this.backgroundColor);
		graphics.fillRect(offsetX, offsetY, textWidth, textHeight);
		graphics.setColor(this.textColor);
		trueTypeFont.drawString(offsetX, offsetY, text, Color.white);
	}

}
