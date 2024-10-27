package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.ExplosionAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

public class DefaultEnemyAttackActionPhase extends EnemyAttackActionPhase {

	@Serial
	private static final long serialVersionUID = -3853130683025678558L;

	public DefaultEnemyAttackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		super(Constants.ENEMY_ATTACK_ACTION_DURATION, fightPhaseResult);
	}

	@Override
	public void onTrigger() {
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		
		String text = "<font:critical> " + String.valueOf(getFightPhaseResult().getDamage());
		
		OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getPlayer(), text);
		
		getFightPhaseResult().getPlayer().lookAtActor(getFightPhaseResult().getEnemy());
		getFightPhaseResult().getEnemy().lookAtActor(getFightPhaseResult().getPlayer());
	}

	@Override
	public void onEnd() {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		Player player = getFightPhaseResult().getPlayer();
		
		int damage = getFightPhaseResult().getDamage();
		
		if (player.getInventory().hasVehicle()) {
			VehicleInventoryItem item = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			Vehicle vehicle = item.getVehicle();
			vehicle.setLife(vehicle.getLife() - damage);
			if (vehicle.dead()) {
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MINE_EXPLOSION, new ExplosionAction(500, player.getPosition()));
				player.getInventory().remove(InventoryItemType.VEHICLE);
			}
		} else {
			player.decreaseLife(damage);
		}
		
	}
}
