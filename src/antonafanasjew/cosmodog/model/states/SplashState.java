package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class SplashState extends CosmodogAbstractState {

	private boolean firstUpdate = true;
	
	private long startTime;
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		DrawingContext dc = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		FontRefToFontTypeMap fontType = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.LoadingOrGameOverOrTheEnd);
		Book textBook = TextPageConstraints.fromDc(dc).textToBook("Loading...", fontType);
		TextBookRendererUtils.renderCenteredLabel(gc, g, textBook);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		if (firstUpdate) {
			ApplicationContext.instance();
			startTime = System.currentTimeMillis();
			firstUpdate = false;
		}
		
		if (System.currentTimeMillis() - startTime > 2000) {
			sbg.enterState(CosmodogStarter.INTRO_STATE_ID);
		}
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.SPLASH_STATE_ID;
	}

}
