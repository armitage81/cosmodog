package antonafanasjew.cosmodog.actions;

import java.io.Serializable;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Defines the contract of an asynchronous action.
 * It is assumed that implementations will update the passed time in the {@link #update(int)} method.
 * {@link #onTrigger()} is assumed to be executed when the action starts.
 * {@link #onUpdate(int, int)} is assumed to be called each time the {@link #update(int)} is called, with times before and after the update. 
 * {@link #onEnd()} us assumed to be called when the action has finished.
 */
public interface AsyncAction extends Serializable {
	
	/**
	 * Returns passed time.
	 * 
	 * @return Time that has passed for this action in milliseconds.
	 */
	int getPassedTime();
	
	/**
	 * @return true if the action has finished.
	 */
	boolean hasFinished();
	
	/**
	 * Callback method that will be executed when the action will be triggered.
	 */
	void onTrigger();
	
	/**
	 * Callback method that will be executed when the action is updated. 
	 * @param before Passed time before the update in milliseconds.
	 * @param after Passed time after the update in milliseconds.
	 */
	void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg);
	
	/**
	 * Callback method that will be executed when the action has finished.
	 * (Normally it will be executed directly on the last {@link #update(int)}) call.
	 */
	void onEnd();
	
	void beforeRegistration();
	
	/**
	 * This callback method will be executed after the action has been unregistered from the action registry.
	 * This provides a way for chaining actions of the same type in the registry.
	 */
	void afterUnregistration();
	
	/**
	 * Updates the action with the new time.
	 * @param millis Time in milliseconds that has passed since the last update.
	 */
	void update(int millis, GameContainer gc, StateBasedGame sbg);
	
	
}
