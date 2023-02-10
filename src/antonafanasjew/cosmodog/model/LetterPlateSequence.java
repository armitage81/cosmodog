package antonafanasjew.cosmodog.model;

import java.io.Serializable;

/**
 * Represents the currently entered sequence of letters in the letter plate puzzle.
 * 
 * When the player presses a letter pressure plate, the corresponding letter is added
 * to the current state. The code word is "SERENITY". 
 * To solve the puzzle, the player must traverse from the plate with the letter 'S'
 * to the one with the letter 'E', then to 'R' etc.
 * 
 * When the player steps on a wrong plate, he will be killed.
 * In this case, the sequence is reset.
 * 
 * After the sequence has been entered correctly, the state stays as "SERENITY" 
 * and cannot be changed anymore.
 *
 */
public class LetterPlateSequence implements Serializable {
	
	private static final long serialVersionUID = 507419787073274221L;

	public static final String FULL_SEQUENCE = "all hope abandon ye who enter here";
	
	public static LetterPlateSequence instance = new LetterPlateSequence();
	
	public static LetterPlateSequence getInstance() {
		return instance;
	}

	private StringBuffer currentSequence = new StringBuffer();

	private LetterPlateSequence() {
		
	}
	
	public void addLetter(char c) {
		if (!sequenceComplete() && sequenceCorrect()) {
			this.currentSequence.append(c);
		}
	}
	
	public void resetSequence() {
		currentSequence.delete(0, currentSequence.length());
	}
	
	public boolean sequenceCorrect() {
		return FULL_SEQUENCE.startsWith(currentSequence.toString());
	}
		
	public boolean sequenceComplete() {
		return FULL_SEQUENCE.equals(currentSequence.toString());
	}
	
	public String getCurrentSequence() {
		return currentSequence.toString();
	}

}
