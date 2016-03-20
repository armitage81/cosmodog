package antonafanasjew.cosmodog.rules.actions;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * This is a rule action that does nothing else than registering an async action in the interface
 * action registry of the cosmodog game object.
 */
public class AsyncActionRegistrationRuleAction extends AbstractRuleAction {

	private static final long serialVersionUID = 6155181340442113928L;

	private AsyncActionType asyncActionType;
	private AsyncAction asyncAction;
	
	public AsyncActionRegistrationRuleAction(AsyncActionType asyncActionType, AsyncAction asyncAction) {
		this.asyncActionType = asyncActionType;
		this.asyncAction = asyncAction;
	}
	
	@Override
	public void execute(GameEvent event) {
		ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry();
		actionRegistry.registerAction(asyncActionType, asyncAction);
	}

}
