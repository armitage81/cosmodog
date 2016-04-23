package antonafanasjew.cosmodog.rules.actions.async;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.InputHandlerType;
import antonafanasjew.cosmodog.actions.AbstractAsyncAction;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.DrawingContextUtils;
import antonafanasjew.cosmodog.writing.io.NarrativeSequenceReaderImpl;
import antonafanasjew.cosmodog.writing.model.NarrativeSequence;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBox;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxContent;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxState;

/**
 * Opens a dialog for the cosmodog game object.
 */
public class DialogAction extends AbstractAsyncAction {

	private static final long serialVersionUID = 4542769916577259433L;

	private String dialogId;

	public DialogAction(String dialogId) {
		this.dialogId = dialogId;
	}
	
	@Override
	public void onTrigger() {
		NarrativeSequenceReaderImpl r = new NarrativeSequenceReaderImpl();
		NarrativeSequence ns = null;
		try {
			ns = r.read(dialogId);
		} catch (IOException e) {
			Log.error("Error while initializing dialog '" + dialogId + "'. Could not read the narrative sequence. " + e.getMessage(), e);
		}
		
		//Take care about these two contexts. They are problematic as they have to be in sync with the
		//DialogBoxRenderer.
		DrawingContext dialogBoxDc = ApplicationContext.instance().getDialogBoxDrawingContext();
		DrawingContext writingDc = DrawingContextUtils.writingContentDcFromDialogBoxDc(dialogBoxDc);
		
		
		
		WritingTextBox writingTextBox = new WritingTextBox(writingDc.w(), writingDc.h(), 0, 3, 18, 22);
		
		WritingTextBoxContent textBoxContent = new WritingTextBoxContent(writingTextBox, ns.getTextBlocks());
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		cosmodogGame.setWritingTextBoxState(new WritingTextBoxState(textBoxContent));
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		int delta = after - before;
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME_DIALOG).handleInput(gc, sbg, delta, applicationContext);
		
		//Update the dialog box' state or unset it in case it has been finished.
		if (cosmodogGame.getWritingTextBoxState().isFinish()) {
			cosmodogGame.setWritingTextBoxState(null);
		} else {
			cosmodogGame.getWritingTextBoxState().update(delta);					
		}
	}
	
	
	@Override
	public boolean hasFinished() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		return cosmodogGame.getWritingTextBoxState() == null;
	}
	
	@Override
	public void cancel() {
		
	}
	
}
