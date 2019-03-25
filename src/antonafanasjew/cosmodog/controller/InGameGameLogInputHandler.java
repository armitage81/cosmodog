package antonafanasjew.cosmodog.controller;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.gamelog.GameLogState;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.DialogWithAlisaTransition;
import antonafanasjew.cosmodog.view.transitions.EndingTransition;
import antonafanasjew.cosmodog.view.transitions.MonolithTransition;

public class InGameGameLogInputHandler extends AbstractInputHandler {

	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		MonolithTransition monolithTransition = ApplicationContextUtils.getCosmodogGame().getMonolithTransition();
		DialogWithAlisaTransition dialogWithAlisaTransition = ApplicationContextUtils.getCosmodogGame().getDialogWithAlisaTransition();
		EndingTransition endingTransition = ApplicationContextUtils.getCosmodogGame().getEndingTransition();
		
				
		Input input = gc.getInput();

		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			
			GameLogState openGameLog = cosmodogGame.getOpenGameLog();
			
			long timestamp = System.currentTimeMillis();
			
			if (monolithTransition != null) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
				
				if (monolithTransition.pageIsDynamic && timestamp - monolithTransition.pageStart < TextBookRenderer.PAGE_APPEARANCE_DURATION) {
					monolithTransition.pageIsDynamic = false;
				} else {
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
					if (openGameLog.onLastPage()) {
						cosmodogGame.setOpenGameLog(null);
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
					} else {
						monolithTransition.pageStart = timestamp;
						openGameLog.nextPage();
						monolithTransition.pageIsDynamic = true;
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
					}
				}
				
			} else if (dialogWithAlisaTransition != null) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
				
				if (dialogWithAlisaTransition.pageIsDynamic && timestamp - dialogWithAlisaTransition.pageStart < TextBookRenderer.PAGE_APPEARANCE_DURATION) {
					dialogWithAlisaTransition.pageIsDynamic = false;
				} else {
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
					if (openGameLog.onLastPage()) {
						cosmodogGame.setOpenGameLog(null);
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
					} else {
						dialogWithAlisaTransition.pageStart = timestamp;
						openGameLog.nextPage();
						dialogWithAlisaTransition.pageIsDynamic = true;
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
					}
				}
			} else if (endingTransition != null) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
				
				if (endingTransition.pageIsDynamic && timestamp - endingTransition.pageStart < TextBookRenderer.PAGE_APPEARANCE_DURATION) {
					endingTransition.pageIsDynamic = false;
				} else {
					if (openGameLog.onLastPage()) {
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
						cosmodogGame.setOpenGameLog(null);
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
					} else {
						endingTransition.pageStart = timestamp;
						openGameLog.nextPage();
						endingTransition.pageIsDynamic = true;
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
					}
				}
			} else if (openGameLog != null) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
				if (openGameLog.onLastPage()) {
					cosmodogGame.setOpenGameLog(null);
				} else {
					openGameLog.nextPage();
				}
			}
		}	

		input.clearKeyPressedRecord();

	}

}
