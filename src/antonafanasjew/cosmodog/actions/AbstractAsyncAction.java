package antonafanasjew.cosmodog.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of an asynchronous action.
 * 
 * This class is ignorant about the concrete actions finish condition.
 * It could be based on the action's duration or on some other conditions depending on the implementation.
 * 
 * This class serves as adapter for the callback methods (it implements them by doing nothing)
 * 
 * See also {@link AsyncAction}
 * 
 */
public abstract class AbstractAsyncAction implements AsyncAction {

	private static final long serialVersionUID = 5748519582133380570L;

	private boolean canceled = false;
	
	private int passedTime = 0;
	
	
	private boolean triggered = false;
	private boolean finished = false;

	private final Map<String, Object> properties = new HashMap<>();

	@Override
	public <T> T getProperty(String name) {
		return (T)properties.get(name);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public int getPassedTime() {
		return passedTime;
	}

	@Override
	public void update(int millis, GameContainer gc, StateBasedGame sbg) {

		if (!triggered) {
			onTrigger();
			triggered = true;
		}
		
		int timeAfterUpdate = passedTime + millis; 
		
		onUpdate(passedTime, timeAfterUpdate, gc, sbg);
		
		passedTime = timeAfterUpdate;
		
		if (hasFinished() && !finished) {
			onEnd();
			finished = true;
		}
		
	}
	
	
	/**
	 * Does nothing per default.
	 */
	@Override
	public void onTrigger() {

	}

	/**
	 * Does nothing per default.
	 * 
	 * @param before No meaning.
	 * @param after No meaning.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {

	}

	/**
	 * Does nothing per default.
	 */
	@Override
	public void onEnd() {

	}

	@Override
	public void beforeRegistration() {
		triggered = false;
		finished = false;
		passedTime = 0;
	}

	/**
	 * Does nothing per default.
	 */
	@Override
	public void afterUnregistration() {
		
	}
	
	/**
	 * Template method to define the finish condition. Will be implemented in sub classes.
	 */
	@Override
	public abstract boolean hasFinished();
	

	public void cancel() {
		this.setCanceled(true);
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
}
