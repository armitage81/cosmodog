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

	private boolean interfaceActionRegistryNotNormalActionRegistry = true;
	
	private AsyncActionType asyncActionType;
	private AsyncAction asyncAction;
	
	public AsyncActionRegistrationRuleAction(AsyncActionType asyncActionType, AsyncAction asyncAction, boolean interfaceActionRegistryNotNormalActionRegistry) {
		this.asyncActionType = asyncActionType;
		this.asyncAction = asyncAction;
		this.interfaceActionRegistryNotNormalActionRegistry = interfaceActionRegistryNotNormalActionRegistry;
	}
	
	public AsyncActionRegistrationRuleAction(AsyncActionType asyncActionType, AsyncAction asyncAction) {
		this(asyncActionType, asyncAction, true);
	}
	
	@Override
	public void execute(GameEvent event) {
		ActionRegistry actionRegistry;
		if (interfaceActionRegistryNotNormalActionRegistry) {
			actionRegistry = ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry();
		} else {
			actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
		}
		actionRegistry.registerAction(asyncActionType, asyncAction);
	}

	public void setInterfaceActionRegistryNotNormalActionRegistry(boolean interfaceActionRegistryNotNormalActionRegistry) {
		this.interfaceActionRegistryNotNormalActionRegistry = interfaceActionRegistryNotNormalActionRegistry;
	}
	
}
