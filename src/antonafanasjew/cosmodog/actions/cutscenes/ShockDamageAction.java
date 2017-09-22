package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class ShockDamageAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -5764923931787835528L;
	
	public static class ShockDamageTransition {

		public int positionX;
		public int positionY;
		public float percentage = 0.0f;

	}

	private ShockDamageTransition transition;
	
	public ShockDamageAction(int duration) {
		super(duration);
		Player player = ApplicationContextUtils.getPlayer();
		this.transition = new ShockDamageTransition();
		this.transition.positionX = player.getPositionX();
		this.transition.positionY = player.getPositionY();
	}

	public ShockDamageTransition getTransition() {
		return transition;
	}

	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		OverheadNotificationAction.registerOverheadNotification(player, "Shock: -1");
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
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		if (player.getLife() > 1) {
			player.decreaseLife(1);
		} else {
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.DEATH_BY_SHOCK, new DeathByShockAction(1000));
		}		
	}
	

}
