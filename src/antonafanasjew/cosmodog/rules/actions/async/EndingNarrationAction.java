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
import antonafanasjew.cosmodog.view.transitions.EndingTransition;
import antonafanasjew.cosmodog.view.transitions.EndingTransition.ActionPhase;

public class EndingNarrationAction extends AbstractNarrationAction {

	private static final long serialVersionUID = 2907704591362301531L;
	
	public EndingNarrationAction(GameLog gameLog) {
		super(gameLog);
	}

	@Override
	public void onTrigger() {
		
		long referenceTime = System.currentTimeMillis();
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		DrawingContext textDc = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneTextDrawingContext();
		String text = getGameLog().getLogText();
		String title = getGameLog().getHeader();
		
		TextPageConstraints tpc = TextPageConstraints.fromDc(textDc);
		Book book = tpc.textToBook(text, FontRefToFontTypeMap.forNarration(), 20);
		
		cosmodogGame.setOpenBook(book);
		cosmodogGame.setOpenBookTitle(title);
		
		EndingTransition transition = new EndingTransition();
		transition.phaseStart = referenceTime;
		cosmodogGame.setEndingTransition(transition);
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		boolean skipCutscenes = Features.getInstance().featureOn(Features.FEATURE_CUTSCENES) == false;
		
		long referenceTime = System.currentTimeMillis();
		
		EndingTransition transition = ApplicationContextUtils.getCosmodogGame().getEndingTransition();
		
		ActionPhase prevActionPhase = transition.phase;
		
		if (transition.phase == ActionPhase.DARKNESS) {
			if (transition.phaseCompletion() >= 1.0f || skipCutscenes) {
				transition.phase = ActionPhase.PICTURE_FADES_IN;
			}
		} else if (transition.phase == ActionPhase.PICTURE_FADES_IN) {
			if (transition.phaseCompletion() >= 1.0f || skipCutscenes) {
				transition.phase = ActionPhase.PICTURE;
			}
		} else if (transition.phase == ActionPhase.PICTURE) {
			if (transition.phaseCompletion() >= 1.0f || skipCutscenes) {
				transition.phase = ActionPhase.PICTURE_FADES_OUT;
			}
		} else if (transition.phase == ActionPhase.PICTURE_FADES_OUT) {
			if (transition.phaseCompletion() >= 1.0f || skipCutscenes) {
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
	public boolean hasFinished() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		return cosmodogGame.getOpenBook() == null;
	}

}
