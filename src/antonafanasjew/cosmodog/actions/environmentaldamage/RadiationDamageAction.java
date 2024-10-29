package antonafanasjew.cosmodog.actions.environmentaldamage;

import antonafanasjew.cosmodog.actions.death.DeathByRadiationAction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

public class RadiationDamageAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5764923931787835528L;

	public static final int DAMAGE = 9;

	public RadiationDamageAction(int duration) {
		super(duration);
		Player player = ApplicationContextUtils.getPlayer();
		this.getProperties().put("position", player.getPosition());
	}

	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_POISONED).play();
		OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> You are exposed to radiation.");
		OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> " + DAMAGE);
	}

	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		if (player.getLife() > DAMAGE) {
			player.decreaseLife(DAMAGE);
		} else {
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.DEATH_BY_RADIATION, new DeathByRadiationAction(1000));
		}		
	}
}
