package antonafanasjew.cosmodog.rules.actions.async;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.InputHandlerType;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.MonolithTransition;
import antonafanasjew.cosmodog.view.transitions.MonolithTransition.ActionPhase;

public class MonolithNarrationAction extends AbstractNarrationAction {

	private static final long serialVersionUID = 6210312310206372546L;

	public MonolithNarrationAction(GameLog gameLog) {
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
		
		MonolithTransition transition = new MonolithTransition();
		transition.phaseStart = referenceTime;		
		cosmodogGame.setMonolithTransition(transition);
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		long referenceTime = System.currentTimeMillis();
		
		MonolithTransition transition = ApplicationContextUtils.getCosmodogGame().getMonolithTransition();
		
		ActionPhase prevActionPhase = transition.phase;
		
		if (Features.getInstance().featureOn(Features.FEATURE_CUTSCENES) == false) {
			if (transition.phase != ActionPhase.TEXT) {
				transition.phase = ActionPhase.TEXT;
				ApplicationContextUtils.getCosmodogGame().getOpenBook().resetTimeAfterPageOpen();
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
			}
		}
		
		if (transition.phase == ActionPhase.MONOLITH_FADES_IN) {
			if (transition.phaseCompletion() >= 1.0f) {
				transition.phase = ActionPhase.MONOLITH;
			}
		} else if (transition.phase == ActionPhase.MONOLITH) {
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
		ApplicationContextUtils.getCosmodogGame().setMonolithTransition(null);
	}
	
	@Override
	public boolean hasFinished() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		return cosmodogGame.getOpenBook() == null;
	}

}
