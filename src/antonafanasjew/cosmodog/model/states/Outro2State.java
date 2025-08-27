package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.GameFlowUtils;
import antonafanasjew.cosmodog.util.MusicUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class Outro2State extends CosmodogAbstractState {

	private Book book;
	
	@Override
	public void everyEnter(GameContainer container, StateBasedGame game) throws SlickException {

		MusicUtils.loopMusic(MusicResources.MUSIC_MAIN_MENU);

		container.getInput().clearKeyPressedRecord();
		GameFlowUtils.updateScoreList();

		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
		
		String text = ApplicationContext.instance().getGameTexts().get("outro").getLogText();
		DrawingContext textDc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameOutroTextDrawingContext();
		TextPageConstraints tpc = TextPageConstraints.fromDc(textDc);
		book = tpc.textToBook(text, FontRefToFontTypeMap.forEmphasizedNarration(), 100);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {

		long referenceTime = System.currentTimeMillis();
		
		Input input = gc.getInput();

		if (input.isKeyPressed(Input.KEY_ENTER)) {

			if (book.isSkipPageBuildUpRequest() || book.dynamicPageComplete(referenceTime)) {

				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();

				if (!book.onLastPage()) {
					book.nextPage();
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
				} else {
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
					int lengthOfFadeOutTransition = 10000;
					sbg.enterState(CosmodogStarter.THE_END_STATE_ID, new FadeOutTransition(Color.white, lengthOfFadeOutTransition), new FadeInTransition());
				}

			} else {
				book.setSkipPageBuildUpRequest(true);
			}
		}
		
		//After processing a loop, clear the record of pressed buttons.
		input.clearKeyPressedRecord();

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		DrawingContext controlsDc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameOutroControlsDrawingContext();
		
		long referenceTime = System.currentTimeMillis();
		
		TextBookRendererUtils.renderDynamicTextPage(gc, g, book);
		
		boolean renderHint = book.dynamicPageComplete(referenceTime);
		boolean renderBlinkingHint = (referenceTime / 250 % 2) == 1;
		if (renderHint && renderBlinkingHint) {
			FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
			Book controlHint = TextPageConstraints.fromDc(controlsDc).textToBook("Press [ENTER]", fontRefToFontTypeMap);
			TextBookRendererUtils.renderCenteredLabel(gc, g, controlHint);
		}

	}

	@Override
	public int getID() {
		return CosmodogStarter.OUTRO2_STATE_ID;
	}

}
