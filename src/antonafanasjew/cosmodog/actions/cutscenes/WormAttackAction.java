package antonafanasjew.cosmodog.actions.cutscenes;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.respawn.RespawnAction;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

/**
 * Represents the game action of the attacking worm animation.
 * A worm attack happens if the player walks on snow in a specific area for too long. It immediately kills the player.
 * <p>
 * This is a fixed length action. It is considered finished when the duration has passed.
 * <p>
 * The action models two effects: The earthquake and the worm attack. The latter is represented by the worm attack transition.
 * This transition mainly defines the height of the worm for every frame, meaning how much of the worm is visible above the snow
 * when it lurches up.
 */
public class WormAttackAction extends FixedLengthAsyncAction {


	@Serial
	private static final long serialVersionUID = 8882906074849186691L;

	/**
	 * Indicates whether the earthquake sound has been triggered (before worm appears).
	 * This is to avoid multiple sound plays during the same action.
	 */
	private boolean triggeredEarthquake = false;

	/**
	 * Indicates whether the worm sound has been triggered (growling).
	 * This is to avoid multiple sound plays during the same action.
	 */
	private boolean triggeredWorm = false;

	/**
	 * Indicates whether the second earthquake sound has been triggered (after worm disappears).
	 * This is to avoid multiple sound plays during the same action.
	 */
	private boolean triggeredExitEarthquake = false;
	
	/**
	 * This transition describes the progress of the attacking worm.
	 * Depending on the percentage of the action duration, it can indicate
	 * the height of the worm which is moving out of the snow, grabs the player
	 * and then pulls itself back into the snow.
	 */
	public static class WormAttackTransition {
		
		/**
		 * Percentage of the action completion. It is a value between 0.0 and 1.0.
		 * It is used to calculate the current height of the worm.
		 */
		public float percentage = 0.0f;
		
		/**
		 * Indicates the current height of the worm.
		 * This value can be used to render the worm animation
		 * at the proper place.
		 * <p>
		 * The value returned here is the rate of the maximal height. It is in the interval between 0..1.
		 * The renderer will move the image vertically by multiplying the actual max height with this value.
		 * <p>
		 * The function time -> height is implemented to make the lurching attack upwards plausible.
		 * It follows the pattern:
		 * - First, there is no worm for a while (20% of the animation time).
		 * - Then, the worm lurches fast upward. (10% of the animation time up to 80% of its max height.)
		 * - Then, the worm's upright movement is delayed. (It rises within 20% of the animation time the last 20% of its max height.)
		 * - For 10% of the animation, the worm stays tall as a tower, not moving.
		 * - Finally, it slowly goes down within the last 40% of the animation time, until it has disappeared under the snow.
		 */
		public float wormHeightPercentage() {

			//First 20% of the animation time, the worm is down.
			if (percentage < 0.2) {
                return (float) 0;
			}

			//Between 20% and 30% of the animation time, the worm lurches upward.
			//It happens linearly. The value returned here is in the interval 0..0.8.
			if (percentage < 0.3) {
                return (percentage - 0.2f) / (0.1f) * 0.8f;
			}

			//Between 30% and 50% of the animation time, the worms movement upward is delayed.
			//Being already at 0.8 percent of the max height at the beginning,
			//it moves in the interval 0.8..1.0 during the 20% of the animation.
			//At the end of this block, half of the animation has passed.
			if (percentage < 0.5) {
                return 0.8f + (percentage - 0.3f) / (0.2f) * 0.2f;
			}

			//For a while, the worm stays still, having caught its prey.
			//Its height is at max for 10% of the animation.
			if (percentage < 0.6) {
                return 1.0f;
			}

			//Finally, the last 40% of the animation time are spent on going back down under the snow.
			//The height of the worm is reduced with every frame until it has disappeared.
			if (percentage < 1.0) {
                return 1.0f - (percentage - 0.6f) / (0.4f);
			}

			//This case happens after the animation already finished and the worm disappeared. Its height rate is 0.0f.
            return 0.0f;
		}
		
	}

	/**
	 * The transition that is used to render the worm attack on the screen.
	 */
	private WormAttackTransition transition;

	/**
	 * Returns the underlying transition.
	 */
	public WormAttackTransition getTransition() {
		return transition;
	}

	/**
	 * Initializes the worm attack action with the given duration.
	 */
	public WormAttackAction(int duration) {
		super(duration);
		transition = new WormAttackTransition();
	}
	
	/**
	 * Updates the action by modifying the underlying transition.
	 * <p>
	 * Take note: The transition only defines, how high the worm should be rendered when it lurches upwards.
	 * But there is also the earthquake sound to be played twice. The first time, it is played at the beginning. The second time,
	 * it is played when the worm starts going down. Additionally, a growling sound from the worm is played once, when the worm appears.
	 * <p>
	 * Since we want to play the sounds only once, we need to keep track of whether they have been played already. That's what the boolean flags are for.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();

		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
				
		getTransition().percentage = actionPercentage;

		//Play the earthquake sound at the beginning.
		if (!triggeredEarthquake) {
			triggeredEarthquake = true;
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EARTHQUAKE).play();
		}

		//Play the earthquake sound again when the worm starts going down.
		if (!triggeredExitEarthquake && actionPercentage > 0.6) {
			triggeredExitEarthquake= true;
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EARTHQUAKE).play();
		}

		//Play the worm growl sound when the worm appears.
		if (!triggeredWorm && transition.wormHeightPercentage() > 0) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_WORM_GROWL).play();
			triggeredWorm = true;
		}
	}
	
	/**
	 * Concludes the action by nullifying the underlying transition.
	 * Additionally, registered the respawning action to respawn the player at the beginning of the worm area.
	 * Take note: In the previous versions, the player has been killed here and respawned at the beginning of the game,
	 * but now the consequence of being eaten by the worm has been mitigated. There is no death sequence but a fading
	 * sequence instead when resetting the player's position.
	 */
	@Override
	public void onEnd() {
		transition = null;
		ApplicationContextUtils.getPlayer().resetTurnsWormAlerted();
		ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.RESPAWNING, new RespawnAction(Position.fromCoordinates(321, 335, MapType.MAIN), false, true));
	}
}
