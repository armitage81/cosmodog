package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class CosmodogAbstractState extends BasicGameState {

	private boolean initialized = false;
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {

		if (!initialized) {
			firstEnter(container, game);
			initialized = true;
		}

		everyEnter(container, game);
		
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		
	}
	
	public void firstEnter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}
	
	public void everyEnter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}
	
	
	
}
