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
import antonafanasjew.cosmodog.view.transitions.EndingTransition;
import antonafanasjew.cosmodog.view.transitions.EndingTransition.ActionPhase;

public class EndingNarrationAction extends AbstractNarrationAction {

	private static final long serialVersionUID = 2907704591362301531L;

	public EndingNarrationAction(GameLog gameLog) {
		super(gameLog);
	}

	@Override
	public void onTrigger() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		DrawingContext cutsceneTextDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneTextDrawingContext();
		cosmodogGame.setOpenGameLog(new GameLogState(getGameLog(), new TextPageConstraints(cutsceneTextDrawingContext.w(), cutsceneTextDrawingContext.h()), FontType.EndingNarration));
		
		EndingTransition transition = new EndingTransition();
		transition.phaseStart = System.currentTimeMillis();
		transition.pageIsDynamic = true;
		cosmodogGame.setEndingTransition(transition);
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		long timestamp = System.currentTimeMillis();
		EndingTransition transition = ApplicationContextUtils.getCosmodogGame().getEndingTransition();
		
		ActionPhase prevActionPhase = transition.phase;
		
		if (transition.phase == ActionPhase.DARKNESS) {
			if (transition.phaseCompletion() >= 1.0f) {
				transition.phase = ActionPhase.PICTURE_FADES_IN;
			}
		} else if (transition.phase == ActionPhase.PICTURE_FADES_IN) {
			if (transition.phaseCompletion() >= 1.0f) {
				transition.phase = ActionPhase.PICTURE;
			}
		} else if (transition.phase == ActionPhase.PICTURE) {
			if (transition.phaseCompletion() >= 1.0f) {
				transition.phase = ActionPhase.PICTURE_FADES_OUT;
			}
		} else if (transition.phase == ActionPhase.PICTURE_FADES_OUT) {
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
	public boolean hasFinished() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		return cosmodogGame.getOpenGameLog() == null;
	}

}
