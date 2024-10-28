package antonafanasjew.cosmodog.actions.teleportation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

public class TeleportStartActionPhase extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -2679131506727529121L;

	public TeleportStartActionPhase(int duration, TeleportationAction.TeleportationState state) {
		super(duration);
		this.getProperties().put("state", state);
	}

	@Override
	public void onTrigger() {
		TeleportationAction.TeleportationState state = this.getProperty("state");
		state.beingTeleported = true;
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TELEPORT_START).play();
	}
	
	@Override
	public void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		int numberOfSlices = 10;
		
		float sliceDuration = 1f / numberOfSlices;
		
		int slice = (int)(getCompletionRate() / sliceDuration);
		boolean oddSlice = slice % 2 == 1;

		boolean lateSlice = slice > 6;

		TeleportationAction.TeleportationState state = this.getProperty("state");
		state.characterVisible = !oddSlice && !lateSlice;
	}
	
	@Override
	public void onEnd() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		TeleportationAction.TeleportationState state = this.getProperty("state");
		state.characterVisible = false;
	}
	
}
