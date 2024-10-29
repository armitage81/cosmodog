package antonafanasjew.cosmodog.controller;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.narration.DialogWithAlisaNarrationAction;
import antonafanasjew.cosmodog.actions.narration.EndingNarrationAction;
import antonafanasjew.cosmodog.actions.narration.MonolithNarrationAction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class InGameGameLogInputHandler extends AbstractInputHandler {

	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {

		long referenceTime = System.currentTimeMillis();
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

		MonolithNarrationAction monolithNarrationAction =
				ApplicationContextUtils
						.getCosmodogGame()
						.getInterfaceActionRegistry()
						.getRegisteredAction(AsyncActionType.MODAL_WINDOW, MonolithNarrationAction.class);

		DialogWithAlisaNarrationAction dialogWithAlisaNarrationAction =
				ApplicationContextUtils
						.getCosmodogGame()
						.getInterfaceActionRegistry()
						.getRegisteredAction(AsyncActionType.MODAL_WINDOW, DialogWithAlisaNarrationAction.class);

		EndingNarrationAction endingNarrationAction =
				ApplicationContextUtils
						.getCosmodogGame()
						.getInterfaceActionRegistry()
						.getRegisteredAction(AsyncActionType.MODAL_WINDOW, EndingNarrationAction.class);

		Book openBook = cosmodogGame.getOpenBook();
		
		Input input = gc.getInput();

		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			
			boolean monolithNarrationText = monolithNarrationAction != null && monolithNarrationAction.phase == MonolithNarrationAction.ActionPhase.TEXT;
			boolean dialogNarrationText = dialogWithAlisaNarrationAction != null && dialogWithAlisaNarrationAction.phase == DialogWithAlisaNarrationAction.ActionPhase.TEXT;
			boolean endingNarrationText = endingNarrationAction != null && endingNarrationAction.phase == EndingNarrationAction.ActionPhase.TEXT;
			
			if (monolithNarrationText || dialogNarrationText || endingNarrationText) {
				
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
				
				if (!openBook.dynamicPageComplete(referenceTime)) {
					openBook.setSkipPageBuildUpRequest(true);
				} else {
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
					if (openBook.onLastPage()) {
						cosmodogGame.setOpenBook(null);
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
					} else {
						openBook.nextPage();
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
					}
				}
				
			} else if (openBook != null) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
				if (openBook.onLastPage()) {
					cosmodogGame.setOpenBook(null);
				} else {
					openBook.nextPage();
				}
			}
		}	

		input.clearKeyPressedRecord();

	}

}
