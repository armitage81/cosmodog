package antonafanasjew.cosmodog.actions;

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

	private int duration;
	
	/**
	 * Initializes the action with the fixed duration.
	 * @param duration Fixed duration.
	 */
	public FixedLengthAsyncAction(int duration) {
		this.duration = duration;
	}

	/**
	 * Returns the duration of this action.
	 * @return Duration of the action.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Returns true if the past time is greater or equals than the defined duration.
	 */
	@Override
	public boolean hasFinished() {
		return isCanceled() || getPassedTime() >= duration;
	}
	
	
	
}
