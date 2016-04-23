package antonafanasjew.cosmodog.rules.actions.async;

import antonafanasjew.cosmodog.actions.AbstractAsyncAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.NarrativeSequenceUtils;

/**
 * Adds a comment for the cosmodog game object.
 */
public class CommentAction extends AbstractAsyncAction {

	private static final long serialVersionUID = 4542769916577259433L;

	private String commentText;

	public CommentAction(String commentText) {
		this.commentText = commentText;
	}
	
	@Override
	public void onTrigger() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(commentText), false, true);
	}

	@Override
	public boolean hasFinished() {
		return true;
	}
	
	@Override
	public void cancel() {
		
	}
	
}
