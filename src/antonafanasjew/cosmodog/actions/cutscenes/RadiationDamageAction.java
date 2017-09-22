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

public class RadiationDamageAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -5764923931787835528L;
	
	public static class RadiationDamageTransition {

		public int positionX;
		public int positionY;
		public float percentage = 0.0f;

	}

	private RadiationDamageTransition transition;
	
	public RadiationDamageAction(int duration) {
		super(duration);
		Player player = ApplicationContextUtils.getPlayer();
		this.transition = new RadiationDamageTransition();
		this.transition.positionX = player.getPositionX();
		this.transition.positionY = player.getPositionY();
	}

	public RadiationDamageTransition getTransition() {
		return transition;
	}

	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_POISONED).play();
		OverheadNotificationAction.registerOverheadNotification(player, "Radiation: -2");
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
		if (player.getLife() > 2) {
			player.decreaseLife(2);
		} else {
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.DEATH_BY_RADIATION, new DeathByRadiationAction(1000));
		}		
	}
	

}
