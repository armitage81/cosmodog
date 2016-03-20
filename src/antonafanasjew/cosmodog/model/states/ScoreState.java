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

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.ScoreEntry;
import antonafanasjew.cosmodog.model.ScoreList;

public class ScoreState extends BasicGameState {

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
			sbg.enterState(CosmodogStarter.MAIN_MENU_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(Color.white);
		
		Cosmodog cosmodog = ApplicationContext.instance().getCosmodog();
		ScoreList scoreList = cosmodog.getScoreList();
		
		if (scoreList.isEmpty()) {
			g.drawString("No records stored yet", 100, 100);
			g.drawString("Press Enter to return to the main menu", 100, 150);
		} else {
			int verticalOffset = 100;
			for (int i = 0; i < scoreList.size(); i++) {
				ScoreEntry scoreEntry = scoreList.get(i);
				String s = scoreEntry.getUserName() + ": " + scoreEntry.getScore();
				g.drawString(s, 100, verticalOffset);
				verticalOffset += 25;
			}
			g.drawString("Press Enter to return to the main menu", 100, verticalOffset);
		}
		


	}

	@Override
	public int getID() {
		return CosmodogStarter.SCORE_STATE_ID;
	}

}
