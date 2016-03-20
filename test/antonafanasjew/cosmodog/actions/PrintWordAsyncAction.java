package antonafanasjew.cosmodog.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Simple test class to demonstrate a variable length action
 *
 */
public class PrintWordAsyncAction extends VariableLengthAsyncAction {

	private static final long serialVersionUID = 156496051342854638L;

	private static final int INTERVAL_BETWEEN_LETTTERS = 2000;
	
	private String text;
	private StringBuffer currentWord = new StringBuffer();

	
	public PrintWordAsyncAction(String text) {
		this.text = text;
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {

		int lettersToPrint = after / INTERVAL_BETWEEN_LETTTERS;
		
		for (int i = currentWord.length(); i < lettersToPrint; i++) {
			currentWord.append(text.charAt(i));
			System.out.print(text.charAt(i));
		}
		
	}
	
	@Override
	public boolean hasFinished() {
		return currentWord.toString().length() == text.length();
	}

}
