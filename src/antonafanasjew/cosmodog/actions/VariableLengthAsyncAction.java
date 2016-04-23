package antonafanasjew.cosmodog.actions;


/**
 * This type of asynchronous action has no fixed length and can take as long as needed
 * till the action object reaches the given state.
 * It is just there to hold common logic for variable length actions. 
 * The implementations need to implement the actual "has finished" condition.
 */
public abstract class VariableLengthAsyncAction extends AbstractAsyncAction {

	private static final long serialVersionUID = -1887040186120949596L;

}
