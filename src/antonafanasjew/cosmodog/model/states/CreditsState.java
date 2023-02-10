package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class CreditsState extends CosmodogAbstractState {

	private String credits;
	
	@Override
	public void firstEnter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		credits = ApplicationContext.instance().getGameTexts().get("credits").getLogText();
	}
	
	@Override
	public void everyEnter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
			sbg.enterState(CosmodogStarter.MAIN_MENU_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		long referenceTime = System.currentTimeMillis();
		
		DrawingContext gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		
		DrawingContext creditsTextDc = new CenteredDrawingContext(gameContainerDrawingContext, 800, 500);
		
		creditsTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 0, 1, 6);
		DrawingContext controlsDc = new TileDrawingContext(gameContainerDrawingContext, 1, 10, 0, 9, 1, 1);
		
		FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.Credits);
		Book creditsBook = TextPageConstraints.fromDc(creditsTextDc).textToBook(credits, fontRefToFontTypeMap);
		TextBookRendererUtils.renderCenteredLabel(gc, g, creditsBook);
		
		
		boolean renderBlinkingHint = (referenceTime / 250 % 2) == 1;
		if (renderBlinkingHint) {
			fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
			Book controlHint = TextPageConstraints.fromDc(controlsDc).textToBook("Press [ENTER]", fontRefToFontTypeMap);
			TextBookRendererUtils.renderCenteredLabel(gc, g, controlHint);
		}
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.CREDITS_STATE_ID;
	}

}
