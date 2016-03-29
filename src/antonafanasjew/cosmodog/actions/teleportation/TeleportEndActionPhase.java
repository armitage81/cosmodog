package antonafanasjew.cosmodog.actions.teleportation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.TeleportationTransition;

public class TeleportEndActionPhase extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -5683528278656049586L;

	public TeleportEndActionPhase(int duration) {
		super(duration);
	}
	
	@Override
	public void onTrigger() {
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TELEPORT_END).play();
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		int numberOfSlices = 9;
		
		int sliceDuration = getDuration() / numberOfSlices;
		
		int slice = after / sliceDuration;
		
		boolean oddSlice = slice % 2 == 1;
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		TeleportationTransition t = cosmodogGame.getTeleportationTransition();
		
		t.characterVisible = oddSlice;
		
	}
	
	@Override
	public void onEnd() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		TeleportationTransition t = cosmodogGame.getTeleportationTransition();
		t.characterVisible = true;
		t.isBeingTeleported = false;
	}


}
