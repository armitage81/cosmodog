package antonafanasjew.cosmodog.actions.dying;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;

public class DyingAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -7371591553509263521L;

	public static class DyingTransition {
		
		public float percentage = 0.0f;
		
		public int animationFrameIndex() {
			
			int frameIndex = 0;
			
			if (percentage < 0.1f) {
				frameIndex = 0;
			}
			
			if (percentage >= 0.075f && percentage < 0.15f) {
				frameIndex = 1;
			}
			
			if (percentage >= 0.15f && percentage < 0.225f) {
				frameIndex = 2;
			}
			
			if (percentage >= 0.225f && percentage < 0.3f) {
				frameIndex = 3;
			}
			
			if (percentage >= 0.3f && percentage < 0.375f) {
				frameIndex = 3;
			}
			
			if (percentage >= 0.375f && percentage < 0.45f) {
				frameIndex = 4;
			}
			
			if (percentage >= 0.45f && percentage < 0.525f) {
				frameIndex = 5;
			}
			
			if (percentage >= 0.525f && percentage < 0.7f) {
				frameIndex = 6;
			}
			
			if (percentage >= 0.7f) {
				frameIndex = 7;
			}
			return frameIndex;
		}
		
	}
	
	private DyingTransition transition;
	
	public DyingAction(int duration) {
		super(duration);
		this.transition = new DyingTransition();
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		transition.percentage = actionPercentage;
	}
	
	@Override
	public void onEnd() {
		CosmodogStarter.instance.enterState(CosmodogStarter.GAME_OVER_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		transition = null;
	}

	public DyingTransition getTransition() {
		return transition;
	}

}
