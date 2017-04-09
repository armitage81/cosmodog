package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.actions.wait.WaitAction;

public class PauseDecoratorAction  extends VariableLengthAsyncAction {

	private static final long serialVersionUID = 5171259029091809078L;

	private int durationBefore;
	private int durationAfter;
	private AsyncAction underlyingAsyncAction;
	
	private ActionRegistry actionPhaseRegistry = new ActionRegistry();
	
	public PauseDecoratorAction(int durationBefore, int durationAfter, AsyncAction underlyingAsyncAction) {
		this.durationBefore = durationBefore;
		this.durationAfter = durationAfter;
		this.underlyingAsyncAction = underlyingAsyncAction;
	}
	
	@Override
	public void onTrigger() {
		
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, new WaitAction(durationBefore));
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, underlyingAsyncAction);
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, new WaitAction(durationAfter));
		
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		actionPhaseRegistry.update(after - before, gc, sbg);
	}
	
	@Override
	public boolean hasFinished() {
		return !actionPhaseRegistry.isActionRegistered(AsyncActionType.CUTSCENE);
	}
	
}
