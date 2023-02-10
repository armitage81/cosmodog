package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class TheEndState extends CosmodogAbstractState {

	public static final int FADE_OUT_DURATION = 3000;
	public static final int DURATION = 10000;
	
	private long startTime;
	
	@Override
	public void everyEnter(GameContainer container, StateBasedGame game) throws SlickException {
		startTime = System.currentTimeMillis();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {

		long timestamp = System.currentTimeMillis();
		
		if (timestamp - startTime > DURATION) {
			sbg.enterState(CosmodogStarter.STATISTICS_STATE_ID, new FadeOutTransition(Color.white, FADE_OUT_DURATION), new FadeInTransition());
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		DrawingContext dc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.LoadingOrGameOverOrTheEnd);
		Book textBook = TextPageConstraints.fromDc(dc).textToBook("The End", fontRefToFontTypeMap);
		TextBookRendererUtils.renderCenteredLabel(gc, g, textBook);
	}

	@Override
	public int getID() {
		return CosmodogStarter.THE_END_STATE_ID;
	}

}
