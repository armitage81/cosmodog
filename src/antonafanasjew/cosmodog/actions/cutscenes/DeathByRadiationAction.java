package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class DeathByRadiationAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -5764923931787835528L;
	
	
	
	public DeathByRadiationAction(int duration) {
		super(duration);
	}

	@Override
	public void onTrigger() {

	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
	}
	
	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.decreaseLife(2);
	}
	

}
