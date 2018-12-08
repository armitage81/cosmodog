package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class AbstractRenderer implements Renderer {

	public void render(GameContainer gameContainer, Graphics graphics) {
		render(gameContainer, graphics, null);
	}
	
	@Override
	public abstract void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter);


}
