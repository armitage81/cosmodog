package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class MineExplosionAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -5764923931787835528L;
	
	public static class MineExplosionTransition {

		public float percentage = 0.0f;

	}

	private MineExplosionTransition transition;
	
	public MineExplosionAction(int duration) {
		super(duration);
		this.transition = new MineExplosionTransition();
	}

	public MineExplosionTransition getTransition() {
		return transition;
	}

	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
		OverheadNotificationAction.registerOverheadNotification(player, "You stepped on a mine.");
		OverheadNotificationAction.registerOverheadNotification(player, String.valueOf(Mine.DAMAGE_TO_PLAYER));
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
		ApplicationContextUtils.getPlayer().decreaseLife(Mine.DAMAGE_TO_PLAYER);
	}
	

}
