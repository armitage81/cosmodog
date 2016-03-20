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

public class CutsceneState extends BasicGameState {

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
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			sbg.enterState(CosmodogStarter.GAME_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		} else if (gc.getInput().isKeyPressed(Input.KEY_W)) {
			sbg.enterState(CosmodogStarter.CREDITS_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.drawString("This is a cut scene", 100, 100);
		g.drawString("Press w to simulate the winning", 100, 150);
		g.drawString("Press Enter to continue game", 100, 200);

	}

	@Override
	public int getID() {
		return CosmodogStarter.CUTSCENE_STATE_ID;
	}

}
