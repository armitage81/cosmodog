package antonafanasjew.cosmodog.actions.cutscenes;

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
		 */
		public float percentage = 0.0f;
		
		/**
		 * Indicates the current height of the worm.
		 * This value can be used to render the worm animation
		 * at the proper place.
		 * <p>
		 * The function time -> height is implemented to make the lurching attack upwards plausible.
		 * It follows the pattern:
		 * - First, there is no worm for a while.
		 * - 
		 */
		public float wormHeightPercentage() {
			
			if (percentage < 0.2) {
                return (float) 0;
			}
			
			if (percentage < 0.3) {
                return (percentage - 0.2f) / (0.1f) * 0.8f;
			}
			
			if (percentage < 0.5) {
                return 0.8f + (percentage - 0.3f) / (0.2f) * 0.2f;
			}
			
			if (percentage < 0.6) {
                return 1.0f;
			}
			
			if (percentage < 1.0) {
                return 1.0f - (percentage - 0.6f) / (0.4f);
			}

            return 0.0f;
		}
		
	}

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
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
				
		getTransition().percentage = actionPercentage;
		
		if (!triggeredEarthquake) {
			triggeredEarthquake = true;
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EARTHQUAKE).play();
		}
		
		if (!triggeredExitEarthquake && actionPercentage > 0.6) {
			triggeredExitEarthquake= true;
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EARTHQUAKE).play();
		}
		
		if (transition.wormHeightPercentage() > 0 && triggeredWorm == false) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_WORM_GROWL).play();
			triggeredWorm = true;
		}
	}
	
	/**
	 * Concludes the action by nullifying the underlying transition.
	 */
	@Override
	public void onEnd() {
		transition = null;
		ApplicationContextUtils.getPlayer().setLife(0);
	}

}
