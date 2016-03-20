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
	
	public TextFieldRenderer(TextField textField) {

		this.textField = textField;
		
	}
	
	
	@Override
	protected void renderFromZero(GameContainer gc, Graphics g, DrawingContext c, Object renderingParameter) {
		g.setColor(Color.white);
		textField.render(gc, g);
	}

}
