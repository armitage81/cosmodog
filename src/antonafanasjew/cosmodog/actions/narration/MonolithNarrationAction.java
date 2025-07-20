package antonafanasjew.cosmodog.actions.narration;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
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

import java.io.Serial;

public class MonolithNarrationAction extends AbstractNarrationAction {

	@Serial
	private static final long serialVersionUID = 6210312310206372546L;

	public static final int DURATION_MONOLITH = 1000;
	public static final int DURATION_PICTURE_FADES_IN = 1000;
	public static final int DURATION_PICTURE_FADES = 2000;

	public static final float MAX_PICTURE_OPACITY = 0.7f;

	public enum ActionPhase {
		MONOLITH_FADES_IN,
		MONOLITH,
		PICTURE_FADES,
		TEXT
	}

	public ActionPhase phase = null;

	public MonolithNarrationAction(GameLog gameLog) {
		super(gameLog);
	}

	@Override
	public void onTriggerInternally() {
		
		long referenceTime = System.currentTimeMillis();
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		String text = getGameLog().getLogText();
		String title = getGameLog().getHeader();
		DrawingContext textDc = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneTextDrawingContext();
		TextPageConstraints tpc = TextPageConstraints.fromDc(textDc);
		Book book = tpc.textToBook(text, FontRefToFontTypeMap.forNarration(), 20);
		
		cosmodogGame.setOpenBook(book);
		cosmodogGame.setOpenBookTitle(title);
		
		phaseStart = referenceTime;
		phase = ActionPhase.MONOLITH_FADES_IN;
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {

		long referenceTime = System.currentTimeMillis();
		
		ActionPhase prevActionPhase = phase;
		
		if (!Features.getInstance().featureOn(Features.FEATURE_CUTSCENES)) {
			if (phase != ActionPhase.TEXT) {
				phase = ActionPhase.TEXT;
				ApplicationContextUtils.getCosmodogGame().getOpenBook().resetTimeAfterPageOpen();
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
			}
		}
		
		if (phase == ActionPhase.MONOLITH_FADES_IN) {
			if (phaseCompletion() >= 1.0f) {
				phase = ActionPhase.MONOLITH;
			}
		} else if (phase == ActionPhase.MONOLITH) {
			if (phaseCompletion() >= 1.0f) {
				phase = ActionPhase.PICTURE_FADES;
			}
		} else if (phase == ActionPhase.PICTURE_FADES) {
			if (phaseCompletion() >= 1.0f) {
				phase = ActionPhase.TEXT;
				ApplicationContextUtils.getCosmodogGame().getOpenBook().resetTimeAfterPageOpen();
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
			}
		} else {
			ApplicationContext applicationContext = ApplicationContext.instance();
			Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
			cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME_GAMELOG).handleInput(gc, sbg, after - before, applicationContext);
		}
		
		if (prevActionPhase != phase) {
			phaseStart = referenceTime;
		}
		
	}
	
	@Override
	public void onEnd() {
		super.onEnd();
	}
	
	@Override
	public boolean hasFinished() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		return cosmodogGame.getOpenBook() == null;
	}

	public long phaseStart;

	public float phaseCompletion() {
		float duration = System.currentTimeMillis() - phaseStart;

		if (phase == ActionPhase.MONOLITH_FADES_IN) {
			return duration < DURATION_PICTURE_FADES_IN ? duration / DURATION_PICTURE_FADES_IN : 1.0f;
		}

		if (phase == ActionPhase.MONOLITH) {
			return duration < DURATION_MONOLITH ? duration / DURATION_MONOLITH : 1.0f;
		}

		if (phase == ActionPhase.PICTURE_FADES) {
			return duration < DURATION_PICTURE_FADES ? duration / DURATION_PICTURE_FADES : 1.0f;
		}

		return 0.0f;
	}

}
