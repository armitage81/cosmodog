package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class SplashState extends CosmodogAbstractState {

	private boolean firstUpdate = true;
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		DrawingContext dc = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		TextBookRendererUtils.renderCenteredLabel(gc, g, dc, "Loading...", FontType.Loading, 0);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		if (firstUpdate) {
			ApplicationContext.instance();
			firstUpdate = false;
		}
		sbg.enterState(CosmodogStarter.INTRO_STATE_ID);
	}

	@Override
	public int getID() {
		return CosmodogStarter.SPLASH_STATE_ID;
	}

}
