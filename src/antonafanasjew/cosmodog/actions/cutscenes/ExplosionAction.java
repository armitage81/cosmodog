package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.cutscenes.MineExplosionAction.MineExplosionTransition;

public class ExplosionAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -5764923931787835528L;
	

	private MineExplosionTransition transition;
	
	public ExplosionAction(int duration, int positionX, int positionY) {
		super(duration);
		this.transition = new MineExplosionTransition();
		this.transition.positionX = positionX;
		this.transition.positionY = positionY;
	}

	public MineExplosionTransition getTransition() {
		return transition;
	}

	@Override
	public void onTrigger() {
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
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
	}
	

}
