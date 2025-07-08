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
	private DrawingContext drawingContext;
	
	public TextRenderer(TrueTypeFont font, boolean centered, DrawingContext drawingContext, Color textColor, Color backgroundColor) {
		this.centered = centered;
		this.trueTypeFont = font;
		this.textHeight = trueTypeFont.getHeight();
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
		this.drawingContext = drawingContext;
	}

	public TextRenderer(TrueTypeFont font, boolean centered, DrawingContext drawingContext) {
		this(font, centered, drawingContext, Color.white, Color.black);
	}
	
	public TextRenderer(DrawingContext drawingContext) {
		this(FONT, false, drawingContext);
	}

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		graphics.translate(drawingContext.x(), drawingContext.y());
		
		String text = (String)renderingParameter;
		this.textWidth = trueTypeFont.getWidth(text);
		
		float offsetX = 0;
		float offsetY = 0;

		if (centered) {

			offsetX = (drawingContext.w() - textWidth) / 2.0f;
			offsetY = (drawingContext.h() - textHeight) / 2.0f;
		}
		graphics.setColor(this.backgroundColor);
		graphics.fillRect(offsetX, offsetY, textWidth, textHeight);
		graphics.setColor(this.textColor);
		trueTypeFont.drawString(offsetX, offsetY, text, Color.white);
		
		graphics.translate(-drawingContext.x(), -drawingContext.y());
	}

}
