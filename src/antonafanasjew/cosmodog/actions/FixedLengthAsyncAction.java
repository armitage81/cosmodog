package antonafanasjew.cosmodog.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.HashMap;
import java.util.Map;

/**
 * This type of asynchronous actions defines a fixed length duration and finishes,
 * when the passed time reaches this duration.
 * 
 * Take note: This class is not abstract as it's super class serves as adapter (implements the callback methods (as doing nothing)).
 * Still, it is recommended in the most cases to sub class this class and reimplement the methods. 
 * 
 */
public class FixedLengthAsyncAction extends AbstractAsyncAction {

	private static final long serialVersionUID = -1032910058049587274L;

	private final int duration;

	private float completionRate;

	public float getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(float completionRate) {
		this.completionRate = completionRate;
	}

	/**
	 * Initializes the action with the fixed duration.
	 * @param duration Fixed duration.
	 */
	public FixedLengthAsyncAction(int duration) {
		this.duration = duration;
		this.completionRate = 0.0f;
	}

	/**
	 * Returns the duration of this action.
	 * @return Duration of the action.
	 */
	public int getDuration() {
		return duration;
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		completionRate = actionPercentage;
		onUpdateInternal(before, after, gc, sbg);
	}

	protected void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {

	}

	/**
	 * Returns true if the past time is greater or equals than the defined duration.
	 */
	@Override
	public boolean hasFinished() {
		return isCanceled() || getPassedTime() >= duration;
	}
	
	
	
}
