package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.MineDetectorInventoryItem;
import antonafanasjew.cosmodog.rules.actions.async.PopUpNotificationAction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class MineDetectorInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		Inventory inventory = player.getInventory();
		
		MineDetectorInventoryItem mineDetectorInventoryItem = (MineDetectorInventoryItem)inventory.get(InventoryItemType.MINEDETECTOR);
		
		if (mineDetectorInventoryItem == null) {
			player.getInventory().put(InventoryItemType.MINEDETECTOR, new MineDetectorInventoryItem());
		} else {
			mineDetectorInventoryItem.increaseDetectionDistance();
			ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry();
			AsyncAction asyncAction = new PopUpNotificationAction("Mine detection distance increased by 1.");
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction);
		}
		
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}
	
}
