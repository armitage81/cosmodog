package antonafanasjew.cosmodog.actions.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.InputHandlerType;
import antonafanasjew.cosmodog.actions.AbstractAsyncAction;
import antonafanasjew.cosmodog.ingamemenu.InGameMenu;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Registers the in game menu as asynchronous action with its own input.
 */
public class InGameMenuAction extends AbstractAsyncAction {

	private static final long serialVersionUID = 4542769916577259433L;

	private InGameMenu inGameMenu;

	public InGameMenuAction(InGameMenu inGameMenu) {
		this.inGameMenu = inGameMenu;
	}
	
	@Override
	public void onTrigger() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		cosmodogGame.setInGameMenu(inGameMenu);
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME_MENU).handleInput(gc, sbg, after - before, applicationContext);
	}
	
	@Override
	public boolean hasFinished() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		return cosmodogGame.getInGameMenu() == null;
	}
	
	
	
}
