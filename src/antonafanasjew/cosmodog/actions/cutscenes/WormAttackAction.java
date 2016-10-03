package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class WormAttackAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = 8882906074849186691L;

	public static class WormAttackTransition {
		public float percentage = 0.0f;
		
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
	
	public WormAttackAction(int duration) {
		super(duration);
		transition = new WormAttackTransition();
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		getTransition().percentage = actionPercentage;
	}
	
	
	@Override
	public void onEnd() {
		transition = null;
		ApplicationContextUtils.getPlayer().setLife(0);
	}

	public WormAttackTransition getTransition() {
		return transition;
	}

}
