package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public interface Renderer {

	void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter);
	
}
