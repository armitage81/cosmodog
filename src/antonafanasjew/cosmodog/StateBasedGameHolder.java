package antonafanasjew.cosmodog;

import org.newdawn.slick.state.StateBasedGame;

/**
 * Potentially, we will need the reference to the StateBasedGame object
 * all over the game. F.i, it could be needed to define the Winning action
 * (to move the game to the next state). As there is no way to retrieve the reference from everywhere,
 * this holder is created. It is containing a static reference to the state based game which is
 * null at the beginning. During the initialization of the game container, the reference
 * will be set and can be used to traverse the game states.
 * 
 * No thread safety!
 */
public class StateBasedGameHolder {

	public static StateBasedGame stateBasedGame;
	
}
