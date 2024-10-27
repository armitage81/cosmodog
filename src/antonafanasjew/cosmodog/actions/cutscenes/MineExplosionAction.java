package antonafanasjew.cosmodog.actions.cutscenes;

import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

public class MineExplosionAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5764923931787835528L;

	public MineExplosionAction(int duration) {
		super(duration);
		Player player = ApplicationContextUtils.getPlayer();
		this.getProperties().put("position", player.getPosition());
	}

	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
		OverheadNotificationAction.registerOverheadNotification(player, "You stepped on a mine.");
		OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> " + String.valueOf(Mine.DAMAGE_TO_PLAYER));
	}

	@Override
	public void onEnd() {

		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		if (player.getInventory().hasVehicle()) {
			VehicleInventoryItem item = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			Vehicle vehicle = item.getVehicle();
			vehicle.setLife(vehicle.getLife() - Mine.DAMAGE_TO_PLAYER);
			if (vehicle.dead()) {
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MINE_EXPLOSION, new ExplosionAction(500, player.getPosition()));
				player.getInventory().remove(InventoryItemType.VEHICLE);
			}
		} else {
			if (Features.getInstance().featureOn(Features.FEATURE_DAMAGE)) {
				player.decreaseLife(Mine.DAMAGE_TO_PLAYER);
			}
		}
	}
}