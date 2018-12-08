package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class LeftInterfaceRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext leftColumnDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().leftColumnDrawingContext();
		
		graphics.setColor(Color.blue);
		graphics.fillRect(leftColumnDrawingContext.x(), leftColumnDrawingContext.y(), leftColumnDrawingContext.w(), leftColumnDrawingContext.h());
	}

}
