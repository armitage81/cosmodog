package antonafanasjew.cosmodog.writing.dynamics;

import java.util.List;

import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.writing.model.TextBlock;

public interface TextBlockDynamicsCalculator {

	public static final int DEFAULT_INTERVAL_BETWEEN_LETTERS = Constants.DEFAULT_INTERVAL_BETWEEN_DIALOG_LETTERS;
	
	/**
	 * Returns the intervals between letters starting before the first letter and ending after the last letter.
	 * f.i. "faster" => 50,40,20,20,20,20,20, that is, 50,f,40,a,20,s,20,t,20,e,20,r,20
	 */
	List<Integer> letterIntervalDurations(TextBlock textBlock);
	
	/**
	 * Returns the duration of the complete dynamics for the block
	 */
	int dynamicsDuration(TextBlock textBlock);
	
}
