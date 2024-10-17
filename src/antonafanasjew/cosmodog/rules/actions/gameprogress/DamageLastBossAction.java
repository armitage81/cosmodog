package antonafanasjew.cosmodog.rules.actions.gameprogress;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.LastBossFightAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SoftwareInventoryItem;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

public class DamageLastBossAction extends AbstractRuleAction {

	@Serial
	private static final long serialVersionUID = 3007391459686874738L;

	@Override
	public void execute(GameEvent event) {

		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Player player = ApplicationContextUtils.getPlayer();

		//Check if the boss has been destroyed already.
		boolean bossDestroyed = false;
		Enemy lastBoss = map.enemyAtTile(Position.fromCoordinates(227, 254));
		if (lastBoss == null) {
			bossDestroyed = true;
		}
		
		
		if (bossDestroyed) {
			OverheadNotificationAction.registerOverheadNotification(player, "The guardian is already destroyed");
		} else {
			//Check if the player has all software chips.
			SoftwareInventoryItem software = (SoftwareInventoryItem)player.getInventory().get(InventoryItemType.SOFTWARE);
			if (software == null || software.getNumber() < Constants.NUMBER_OF_SOFTWARE_PIECES_IN_GAME) {
				OverheadNotificationAction.registerOverheadNotification(player, "Needs " + String.valueOf(Constants.NUMBER_OF_SOFTWARE_PIECES_IN_GAME) + " software chips.");	
			} else {
				OverheadNotificationAction.registerOverheadNotification(player, "Software complete.");
				OverheadNotificationAction.registerOverheadNotification(player, "Guardian deactivated.");
				//Register the fight action for the Guardian.
				AsyncAction asyncAction = new LastBossFightAction(lastBoss);
				//asyncAction = new PauseDecoratorAction(500, 500, asyncAction);
				//asyncAction = new CamCenteringDecoratorAction(1000, 227, 254, asyncAction, ApplicationContextUtils.getCosmodogGame());
				ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
				actionRegistry.registerAction(AsyncActionType.FIGHT, asyncAction);
			}
		}
	}

}
