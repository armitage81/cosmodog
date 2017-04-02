package antonafanasjew.cosmodog.rules.actions.pickupitems;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.KeyInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.PieceInteraction;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.KeyRingInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Key;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class PickupKeyAction extends AbstractRuleAction {

	private static final long serialVersionUID = -2529847901143434996L;
	
	private DoorType keyType;
	
	public PickupKeyAction(DoorType keyType) {
		this.keyType = keyType;
	}

	@Override
	public void execute(GameEvent event) {

		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		Inventory inventory = player.getInventory();
		KeyRingInventoryItem keyring = (KeyRingInventoryItem)inventory.get(InventoryItemType.KEY_RING);
		if (keyring == null) {
			keyring = new KeyRingInventoryItem();
			inventory.put(InventoryItemType.KEY_RING, keyring);
		}
		
		Key key = new Key();
		key.setDoorType(keyType);
		keyring.addKey(key);
		
		CollectibleKey collectibleKey = new CollectibleKey(key);
		
		PieceInteraction keyInteraction = new KeyInteraction();
		keyInteraction.beforeInteraction(collectibleKey, ApplicationContext.instance(), cosmodogGame, player);
		keyInteraction.interactWithPiece(collectibleKey, ApplicationContext.instance(), cosmodogGame, player);
		keyInteraction.afterInteraction(collectibleKey, ApplicationContext.instance(), cosmodogGame, player);
	}

}
