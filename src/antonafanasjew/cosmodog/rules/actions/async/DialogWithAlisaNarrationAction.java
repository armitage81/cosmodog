package antonafanasjew.cosmodog.rules.actions.async;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.InputHandlerType;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.DialogWithAlisaTransition;
import antonafanasjew.cosmodog.view.transitions.DialogWithAlisaTransition.ActionPhase;

public class DialogWithAlisaNarrationAction extends AbstractNarrationAction {

	private static final long serialVersionUID = -5431621300747840836L;
	
	public DialogWithAlisaNarrationAction(GameLog gameLog) {
		super(gameLog);
	}

	@Override
	public void onTrigger() {
		
		long referenceTime = System.currentTimeMillis();
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		String text = getGameLog().getLogText();
		String title = getGameLog().getHeader();
		DrawingContext textDc = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneTextDrawingContext();
		TextPageConstraints tpc = TextPageConstraints.fromDc(textDc);
		Book book = tpc.textToBook(text, FontRefToFontTypeMap.forNarration(), 20);
		
		cosmodogGame.setOpenBook(book);
		cosmodogGame.setOpenBookTitle(title);

		DialogWithAlisaTransition transition = new DialogWithAlisaTransition();
		transition.phaseStart = referenceTime;
		cosmodogGame.setDialogWithAlisaTransition(transition);
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_CUTSCENE_ALISASMESSAGE).play();
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		long referenceTime = System.currentTimeMillis();

		DialogWithAlisaTransition transition = ApplicationContextUtils.getCosmodogGame().getDialogWithAlisaTransition();
		
		ActionPhase prevActionPhase = transition.phase;
		
		if (transition.phase == ActionPhase.ARM_APPEARS) {
			if (transition.phaseCompletion() >= 1.0f) {
				transition.phase = ActionPhase.DEVICE_TURNS_ON;
			}
		} else if (transition.phase == ActionPhase.DEVICE_TURNS_ON) {
			if (transition.phaseCompletion() >= 1.0f) {
				transition.phase = ActionPhase.PICTURE_FADES;
			}
		} else if (transition.phase == ActionPhase.PICTURE_FADES) {
			if (transition.phaseCompletion() >= 1.0f) {
				transition.phase = ActionPhase.TEXT;
				ApplicationContextUtils.getCosmodogGame().getOpenBook().resetTimeAfterPageOpen();
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
			}
		} else {
			ApplicationContext applicationContext = ApplicationContext.instance();
			Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
			cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME_GAMELOG).handleInput(gc, sbg, after - before, applicationContext);
		}
		
		if (prevActionPhase != transition.phase) {
			transition.phaseStart = referenceTime;
		}
		
	}
	
	@Override
	public void onEnd() {
		super.onEnd();
		ApplicationContextUtils.getCosmodogGame().setDialogWithAlisaTransition(null);
	}
	
	@Override
	public boolean hasFinished() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		return cosmodogGame.getOpenBook() == null;
	}
	
	
}
