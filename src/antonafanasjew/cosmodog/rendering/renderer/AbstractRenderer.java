package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;

/**
 * Use this class generally to render parts of the game.
 * The renderer is using the drawing context to know where to draw.
 * The sub classes should implement the rendering as if it would be
 * rendered from 0/0 coordinates
 * 
 * Don't use nested abstract renderers as it will break the translation (the inner renderers will use absolute translation while 
 * still within the outer renderers translation session. Nested renderers should be directly implementing the renderer interface)
 * 
 */
public abstract class AbstractRenderer implements Renderer {

	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext) {
		render(gameContainer, graphics, drawingContext, null);
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		graphics.translate(drawingContext.x(), drawingContext.y());
		renderFromZero(gameContainer, graphics, drawingContext, renderingParameter);
		graphics.translate(-drawingContext.x(), -drawingContext.y());
	}

	protected abstract void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter);

}
