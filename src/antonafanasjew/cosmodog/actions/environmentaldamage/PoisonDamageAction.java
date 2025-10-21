package antonafanasjew.cosmodog.actions.environmentaldamage;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.fight.ExplosionAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

public class PoisonDamageAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5764923931787835528L;

	public PoisonDamageAction(int duration) {
		super(duration);
		Player player = ApplicationContextUtils.getPlayer();
		this.getProperties().put("position", player.getPosition());
	}

	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		String text = "<font:critical> YOU SUCCUMB TO POISON.";
		OverheadNotificationAction.registerOverheadNotification(player, text);
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_POISONED).play();
	}

	@Override
	public void onEnd() {
		ApplicationContextUtils.getPlayer().setLife(0);
	}
}