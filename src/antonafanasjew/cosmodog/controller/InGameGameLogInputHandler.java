package antonafanasjew.cosmodog.controller;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.DialogWithAlisaTransition;
import antonafanasjew.cosmodog.view.transitions.EndingTransition;
import antonafanasjew.cosmodog.view.transitions.MonolithTransition;

public class InGameGameLogInputHandler extends AbstractInputHandler {

	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {

		long referenceTime = System.currentTimeMillis();
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

		/*
		 * openBook can refer to one of the narration actions: dialog with Alisa, ending or monolith.
		 * In these cases, one of the three transitions will not be null.
		 * 
		 * If all of the transitions are null, then openBook refers to a game log in a text frame.
		 * 
		 * openBook can also be null. In this case, the input must not need to be handeld.
		 * 
		 */
		MonolithTransition monolithTransition = cosmodogGame.getMonolithTransition();
		DialogWithAlisaTransition dialogWithAlisaTransition = cosmodogGame.getDialogWithAlisaTransition();
		EndingTransition endingTransition = cosmodogGame.getEndingTransition();

		Book openBook = cosmodogGame.getOpenBook();
		
		Input input = gc.getInput();

		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			
			boolean monolithTransitionText = monolithTransition != null && monolithTransition.phase == MonolithTransition.ActionPhase.TEXT;
			boolean dialogTransitionText = dialogWithAlisaTransition != null && dialogWithAlisaTransition.phase == DialogWithAlisaTransition.ActionPhase.TEXT;
			boolean endingTransitionText = endingTransition != null && endingTransition.phase == EndingTransition.ActionPhase.TEXT;
			
			if (monolithTransitionText || dialogTransitionText || endingTransitionText) {
				
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
