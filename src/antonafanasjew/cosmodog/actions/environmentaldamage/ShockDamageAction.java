package antonafanasjew.cosmodog.actions.environmentaldamage;

import antonafanasjew.cosmodog.actions.death.DeathByShockAction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

public class ShockDamageAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5764923931787835528L;

	public static final int DAMAGE = 1;


	public ShockDamageAction(int duration) {
		super(duration);
		Player player = ApplicationContextUtils.getPlayer();
		this.getProperties().put("position", player.getPosition());
	}

	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> You were electrocuted.");
		OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> " + DAMAGE);
	}

	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		if (player.getLife() > DAMAGE) {
			player.decreaseLife(DAMAGE);
		} else {
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.DEATH_BY_SHOCK, new DeathByShockAction(1000));
		}		
	}
}
