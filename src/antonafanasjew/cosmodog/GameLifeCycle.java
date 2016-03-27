package antonafanasjew.cosmodog;

/**
 * Defines the life cycle of the game. 
 */
public class GameLifeCycle {

	private boolean startNewGame = true;

	/**
	 * Returns the flag to start new game. This happens when the player character dies or a new game has been started.
	 * @return Flag to start new game.
	 */
	public boolean isStartNewGame() {
		return startNewGame;
	}

	/**
	 * Sets the flag to start new game. This happens when the player character dies or a new game has been started.
	 * @param startNewGame Flag to start new game.
	 */
	public void setStartNewGame(boolean startNewGame) {
		this.startNewGame = startNewGame;
	}
}
