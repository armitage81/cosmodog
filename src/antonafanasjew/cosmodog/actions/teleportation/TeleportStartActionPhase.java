package antonafanasjew.cosmodog.actions.teleportation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.TeleportationTransition;

public class TeleportStartActionPhase extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -2679131506727529121L;

	public TeleportStartActionPhase(int duration) {
		super(duration);
	}

	@Override
	public void onTrigger() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		TeleportationTransition t = cosmodogGame.getTeleportationTransition();
		t.isBeingTeleported = true;
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TELEPORT_START).play();
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		int numberOfSlices = 10;
		
		int sliceDuration = getDuration() / numberOfSlices;
		
		int slice = after / sliceDuration;
		boolean oddSlice = slice % 2 == 1;

		boolean lateSlice = slice > 6;
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		TeleportationTransition t = cosmodogGame.getTeleportationTransition();
		
		t.characterVisible = !oddSlice && !lateSlice;
	}
	
	@Override
	public void onEnd() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		TeleportationTransition t = cosmodogGame.getTeleportationTransition();
		t.characterVisible = false;
	}
	
}
