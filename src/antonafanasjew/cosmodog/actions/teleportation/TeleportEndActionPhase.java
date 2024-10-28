package antonafanasjew.cosmodog.actions.teleportation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;

import java.io.Serial;

public class TeleportEndActionPhase extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5683528278656049586L;

	public TeleportEndActionPhase(int duration, TeleportationAction.TeleportationState state) {
		super(duration);
		this.getProperties().put("state", state);
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

		TeleportationAction.TeleportationState state = this.getProperty("state");

		state.characterVisible = oddSlice;
		
	}
	
	@Override
	public void onEnd() {
		TeleportationAction.TeleportationState state = this.getProperty("state");
		state.characterVisible = true;
		state.beingTeleported = false;
	}


}
