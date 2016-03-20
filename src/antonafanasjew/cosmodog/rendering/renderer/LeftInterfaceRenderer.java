package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class LeftInterfaceRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext context, Object renderingParameter) {
		graphics.setColor(Color.blue);
		graphics.fillRect(0, 0, context.w(), context.h());
	}

}
