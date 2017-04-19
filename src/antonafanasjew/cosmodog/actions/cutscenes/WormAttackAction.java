package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Represents the game action of the attacking worm animation. 
 *
 */
public class WormAttackAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = 8882906074849186691L;

	private boolean triggeredEarthquake = false;
	private boolean triggeredWorm = false;
	private boolean triggeredExitEarthquake = false;
	
	/**
	 * This transition describes the progress of the attacking worm.
	 * Depending on the percentage of the action duration, it can indicate
	 * the height of the worm which is moving out of the snow, grabs the player
	 * and then pulls itself back into the snow.
	 */
	public static class WormAttackTransition {
		
		/**
		 * Percentage of the action completion.
		 */
		public float percentage = 0.0f;
		
		/**
		 * Indicates the current height of the worm.
		 * This value can be used to render the worm animation
		 * at the proper place.
		 */
		public float wormHeightPercentage() {
			
			if (percentage < 0.2) {
				float retVal = 0;
				return retVal;
			}
			
			if (percentage < 0.3) {
				float retVal = (percentage - 0.2f) / (0.1f) * 0.8f;
				return retVal;
			}
			
			if (percentage < 0.5) {
				float retVal = 0.8f + (percentage - 0.3f) / (0.2f) * 0.2f;
				return retVal;
			}
			
			if (percentage < 0.6) {
				float retVal = 1.0f;
				return retVal;
			}
			
			if (percentage < 1.0) {
				float retVal = 1.0f - (percentage - 0.6f) / (0.4f) * 1.0f;
				return retVal;
			}
			
			float retVal = 0.0f;
			return retVal;
		}
		
	}

	private WormAttackTransition transition;
	
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

	/**
	 * Returns the underlying transition.
	 */
	public WormAttackTransition getTransition() {
		return transition;
	}

}
