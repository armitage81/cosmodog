package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.TextField;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;

/**
 * Cannot initialize the field bounds in the constructor as the context parameter are passed later.
 * So doing a one time initialization resetting the bounds in the render method.
 */
public class TextFieldRenderer extends AbstractRenderer {
	
	private TextField textField;
	private DrawingContext drawingContext;
	
	public TextFieldRenderer(TextField textField, DrawingContext drawingContext) {

		this.textField = textField;
		this.drawingContext = drawingContext;
		
	}
	
	
	@Override
	public void renderInternally(GameContainer gc, Graphics g, Object renderingParameter) {
		
		g.translate(drawingContext.x(), drawingContext.y());
		
		g.setColor(Color.white);
		textField.render(gc, g);
		
		g.translate(-drawingContext.x(), -drawingContext.y());
	}

}
