package antonafanasjew.cosmodog.actions.death;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;
import java.util.Map;

public class WormAttackAction extends FixedLengthAsyncAction {


	@Serial
	private static final long serialVersionUID = 8882906074849186691L;

	private boolean triggeredEarthquake = false;

	private boolean triggeredWorm = false;

	private boolean triggeredExitEarthquake = false;

	private TiledObject wormRegion;

	public WormAttackAction(int duration, TiledObject wormRegion) {
		super(duration);
		this.wormRegion = wormRegion;
	}
	
	@Override
	public void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {

		//Play the earthquake sound at the beginning.
		if (!triggeredEarthquake) {
			triggeredEarthquake = true;
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EARTHQUAKE).play();
		}

		//Play the earthquake sound again when the worm starts going down.
		if (!triggeredExitEarthquake && getCompletionRate() > 0.6) {
			triggeredExitEarthquake= true;
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EARTHQUAKE).play();
		}

		//Play the worm growl sound when the worm appears.
		if (!triggeredWorm && wormHeightPercentage() > 0) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_WORM_GROWL).play();
			triggeredWorm = true;
		}
	}
	
	@Override
	public void onEnd() {

		Map<String, MapDescriptor> mapDescriptors = ApplicationContextUtils.mapDescriptors();

		ApplicationContextUtils.getPlayer().resetTurnsWormAlerted();

		String respawnPositionInfo = wormRegion.getProperties().get("respawnPosition"); //Has the format "x/y/mapType".

		int x = Integer.parseInt(respawnPositionInfo.split("/")[0]);
		int y = Integer.parseInt(respawnPositionInfo.split("/")[1]);
		String mapName = respawnPositionInfo.split("/")[2];
		MapDescriptor mapDescriptor = mapDescriptors.get(mapName);
		ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.RESPAWNING, new RespawnAction(Position.fromCoordinates(x, y, mapDescriptor), false, true));
	}

	public float wormHeightPercentage() {

		//First 20% of the animation time, the worm is down.
		if (getCompletionRate() < 0.2) {
			return (float) 0;
		}

		//Between 20% and 30% of the animation time, the worm lurches upward.
		//It happens linearly. The value returned here is in the interval 0..0.8.
		if (getCompletionRate() < 0.3) {
			return (getCompletionRate() - 0.2f) / (0.1f) * 0.8f;
		}

		//Between 30% and 50% of the animation time, the worms movement upward is delayed.
		//Being already at 0.8 percent of the max height at the beginning,
		//it moves in the interval 0.8..1.0 during the 20% of the animation.
		//At the end of this block, half of the animation has passed.
		if (getCompletionRate() < 0.5) {
			return 0.8f + (getCompletionRate() - 0.3f) / (0.2f) * 0.2f;
		}

		//For a while, the worm stays still, having caught its prey.
		//Its height is at max for 10% of the animation.
		if (getCompletionRate() < 0.6) {
			return 1.0f;
		}

		//Finally, the last 40% of the animation time are spent on going back down under the snow.
		//The height of the worm is reduced with every frame until it has disappeared.
		if (getCompletionRate() < 1.0) {
			return 1.0f - (getCompletionRate() - 0.6f) / (0.4f);
		}

		//This case happens after the animation already finished and the worm disappeared. Its height rate is 0.0f.
		return 0.0f;
	}
}
