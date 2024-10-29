package antonafanasjew.cosmodog.actions.narration;

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

import java.io.Serial;

public class EndingNarrationAction extends AbstractNarrationAction {

	@Serial
	private static final long serialVersionUID = 2907704591362301531L;

	public static final int DURATION_DARKNESS = 3000;
	public static final int DURATION_PICTURE_FADES_IN = 15000;
	public static final int DURATION_PICTURE = 5000;
	public static final int DURATION_PICTURE_FADES_OUT = 500;
	public static final float INITIAL_PICTURE_OPACITY = 1f;
	public static final float TEXT_PICTURE_OPACITY = 0.7f;

	public enum ActionPhase {
		DARKNESS,
		PICTURE_FADES_IN,
		PICTURE,
		PICTURE_FADES_OUT,
		TEXT
	}

	public ActionPhase phase = ActionPhase.DARKNESS;

	public long phaseStart;

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
		
		phaseStart = referenceTime;
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		boolean skipCutscenes = !Features.getInstance().featureOn(Features.FEATURE_CUTSCENES);
		
		long referenceTime = System.currentTimeMillis();
		
		ActionPhase prevActionPhase = phase;
		
		if (phase == ActionPhase.DARKNESS) {
			if (phaseCompletion() >= 1.0f || skipCutscenes) {
				phase = ActionPhase.PICTURE_FADES_IN;
			}
		} else if (phase == ActionPhase.PICTURE_FADES_IN) {
			if (phaseCompletion() >= 1.0f || skipCutscenes) {
				phase = ActionPhase.PICTURE;
			}
		} else if (phase == ActionPhase.PICTURE) {
			if (phaseCompletion() >= 1.0f || skipCutscenes) {
				phase = ActionPhase.PICTURE_FADES_OUT;
			}
		} else if (phase == ActionPhase.PICTURE_FADES_OUT) {
			if (phaseCompletion() >= 1.0f || skipCutscenes) {
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
	public boolean hasFinished() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		return cosmodogGame.getOpenBook() == null;
	}

	public float phaseCompletion() {

		float duration = System.currentTimeMillis() - phaseStart;

		if (phase == ActionPhase.DARKNESS) {
			return duration < DURATION_DARKNESS ? duration / DURATION_DARKNESS : 1.0f;
		}

		if (phase == ActionPhase.PICTURE_FADES_IN) {
			return duration < DURATION_PICTURE_FADES_IN ? duration / DURATION_PICTURE_FADES_IN : 1.0f;
		}

		if (phase == ActionPhase.PICTURE) {
			return duration < DURATION_PICTURE ? duration / DURATION_PICTURE : 1.0f;
		}

		if (phase == ActionPhase.PICTURE_FADES_OUT) {
			return duration < DURATION_PICTURE_FADES_OUT ? duration / DURATION_PICTURE_FADES_OUT : 1.0f;
		}

		return 0.0f;
	}

}
