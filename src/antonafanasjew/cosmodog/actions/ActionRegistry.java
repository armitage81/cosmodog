package antonafanasjew.cosmodog.actions;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * A class to maintain current asynchronous actions.
 * Each action type has to be stackable or can be registered only once at a time.
 * With each call of the update method, the registry updates all registered
 * actions and unregisters them if they have finished.
 * 
 * (The idea was to queue actions of the same type while allowing multiple actions of different types.
 * f.i. it will be not possible to react on player movement input while player is still moving)
 * 
 * Take care: This class is not thread-safe. it is assumed that only one thread accesses it.
 */
public class ActionRegistry implements Serializable {

	
	private static final long serialVersionUID = 4443341945867939931L;
	

	private ArrayListMultimap<AsyncActionType, AsyncAction> actions = ArrayListMultimap.create();
	
	/**
	 * Registers the action of the given type, but only if no other action of the same type is registered or the action type is stackable.
	 * 
	 * @param actionType type of the action.
	 * @param action action that should be registered.
	 */
	public void registerAction(AsyncActionType actionType, AsyncAction action) {
		if (actionType.isStackable() || !isActionRegistered(actionType)) {
			action.beforeRegistration();
			actions.put(actionType, action);
		}
	}
	
	/**
	 * Indicates whether an action of the given type is registered.
	 * 
	 * @param actionType type of the action.
	 * @return true if action of this type is registered, else otherwise.
	 */
	public boolean isActionRegistered(AsyncActionType actionType) {
		return getRegisteredAction(actionType) != null;
	}
	

	/**
	 * Updates all registered actions for the given milliseconds
	 * and removes actions that have been finished.
	 * 
	 * @param millis time that has passed since the last update in milliseconds.
	 */
	public void update(int millis, GameContainer gc, StateBasedGame sbg) {
		
		Set<AsyncActionType> keysCopy = Sets.newHashSet();
		keysCopy.addAll(actions.keySet());

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		boolean interfaceBlocked = cosmodogGame.getInterfaceActionRegistry().getRegisteredAction(AsyncActionType.BLOCKING_INTERFACE) != null;
		
		for (AsyncActionType key : keysCopy) {

			if (interfaceBlocked && key.isBlockableByInterface()) {
				continue;
			}
			
			List<AsyncAction> actionsWithType = actions.get(key);
			
			if (actionsWithType.isEmpty() == false) {
				AsyncAction firstActionWithType = actionsWithType.get(0);
				
				//Take care: In case of stackable actions we ignore the remaining time for the next action here
				//but this should be fine as long as the update method intervals are short enough.
				firstActionWithType.update(millis, gc, sbg); 
    			if (firstActionWithType.hasFinished()) {
    				actionsWithType.remove(0);
    				firstActionWithType.afterUnregistration();
    			}
			}

		}
		
	}

	/**
	 * Returns the registered action of the given type or null.
	 * In case of stackable actions, the first action in the queue will be returned.
	 * 
	 * @param actionType type of the action.
	 * @return The registered action of the given type or null if none.
	 */
	public AsyncAction getRegisteredAction(AsyncActionType actionType) {
		List<AsyncAction> actionList = Lists.newArrayList(actions.get(actionType));
		AsyncAction action = actionList.isEmpty() ? null : actionList.get(0);
		return action;
	}
	
	/**
	 * Indicates whether the input is blocked. It is the case if at least one action is registered in the registry
	 * with the type that is blocking input. 
	 * 
	 * @return true if input should be blocked, false otherwise.
	 */
	public boolean inputBlocked() {
		
		boolean retVal = false;
		
		for (AsyncActionType key : actions.keySet()) {
			
			if (key.isBlocksInput()) {
				Collection<AsyncAction> actionsWithType = actions.get(key);
				if (actionsWithType.isEmpty() == false) {
					retVal = true;
					break;
				}
			}

		}
		
		return retVal;
	}

}
