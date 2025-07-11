package antonafanasjew.cosmodog.actions.narration;

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

import java.io.Serial;

public class DialogWithAlisaNarrationAction extends AbstractNarrationAction {

	@Serial
	private static final long serialVersionUID = -5431621300747840836L;

	public static final int DURATION_ARM_APPEARS = 1000;
	public static final int DURATION_DEVICE_TURNS_ON = 1000;
	public static final int DURATION_PICTURE_FADES = 10;
	public static final float MAX_PICTURE_OPACITY = 0.9f;

	public enum ActionPhase {
		ARM_APPEARS,
		DEVICE_TURNS_ON,
		PICTURE_FADES,
		TEXT
	}

	public ActionPhase phase = ActionPhase.ARM_APPEARS;

	public long phaseStart;

	public DialogWithAlisaNarrationAction(GameLog gameLog) {
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
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_CUTSCENE_ALISASMESSAGE).play();
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		long referenceTime = System.currentTimeMillis();

		ActionPhase prevActionPhase = phase;
		
		if (phase == ActionPhase.ARM_APPEARS) {
			if (phaseCompletion() >= 1.0f) {
				phase = ActionPhase.DEVICE_TURNS_ON;
			}
		} else if (phase == ActionPhase.DEVICE_TURNS_ON) {
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

	public float phaseCompletion() {
		float duration = System.currentTimeMillis() - phaseStart;

		if (phase == ActionPhase.ARM_APPEARS) {
			return duration < DURATION_ARM_APPEARS ? duration / DURATION_ARM_APPEARS : 1.0f;
		}

		if (phase == ActionPhase.DEVICE_TURNS_ON) {
			return duration < DURATION_DEVICE_TURNS_ON ? duration / DURATION_DEVICE_TURNS_ON : 1.0f;
		}

		if (phase == ActionPhase.PICTURE_FADES) {
			return duration < DURATION_PICTURE_FADES ? duration / DURATION_PICTURE_FADES : 1.0f;
		}

		return 0.0f;
	}
}
