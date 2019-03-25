package antonafanasjew.cosmodog.rules.actions.async;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.InputHandlerType;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogState;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
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
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		DrawingContext cutsceneTextDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneTextDrawingContext();
		cosmodogGame.setOpenGameLog(new GameLogState(getGameLog(), new TextPageConstraints(cutsceneTextDrawingContext.w(), cutsceneTextDrawingContext.h()), FontType.CutsceneNarration));
		DialogWithAlisaTransition transition = new DialogWithAlisaTransition();
		transition.phaseStart = System.currentTimeMillis();
		transition.pageIsDynamic = true;
		cosmodogGame.setDialogWithAlisaTransition(transition);
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_CUTSCENE_ALISASMESSAGE).play();
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		long timestamp = System.currentTimeMillis();
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
				transition.pageStart = System.currentTimeMillis();
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
			}
		} else {
			ApplicationContext applicationContext = ApplicationContext.instance();
			Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
			cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME_GAMELOG).handleInput(gc, sbg, after - before, applicationContext);
		}
		
		if (prevActionPhase != transition.phase) {
			transition.phaseStart = timestamp;
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
		return cosmodogGame.getOpenGameLog() == null;
	}
	
	
}
