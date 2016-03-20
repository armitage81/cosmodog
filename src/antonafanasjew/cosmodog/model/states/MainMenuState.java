package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.CosmodogStarter;

public class MainMenuState extends BasicGameState {

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_1)) {
			sbg.enterState(CosmodogStarter.GAME_INTRO_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		} else if (gc.getInput().isKeyPressed(Input.KEY_2)) {
			sbg.enterState(CosmodogStarter.GAME_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		} else if (gc.getInput().isKeyPressed(Input.KEY_3)) {
			sbg.enterState(CosmodogStarter.SCORE_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		} else if (gc.getInput().isKeyPressed(Input.KEY_4)) {
			System.exit(0);
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.drawString("Cosmodog", 100, 100);
		g.drawString("Press 1 to start the game", 100, 150);
		g.drawString("Press 2 to load a game", 100, 200);
		g.drawString("Press 3 to see the records", 100, 250);
		g.drawString("Press 4 to quit to operating system", 100, 300);

	}

	@Override
	public int getID() {
		return CosmodogStarter.MAIN_MENU_STATE_ID;
	}

}
