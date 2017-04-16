package antonafanasjew.cosmodog.rules.actions.gameprogress;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventChangedPosition;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class FoundSecretAction extends AbstractRuleAction {

	private static final long serialVersionUID = -3586730443484902269L;

	@Override
	public void execute(GameEvent event) {
		
		if (!(event instanceof GameEventChangedPosition)) {
			return;
		}
		
		Player player = ApplicationContextUtils.getPlayer();
		OverheadNotificationAction.registerOverheadNotification(player, "SECRET FOUND");
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SECRET_FOUND).play();
		player.getGameProgress().increaseNumberOfFoundSecrets();
		
	}

}
