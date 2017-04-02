package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.KeyRingInventoryItem;

public class KeyInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		InventoryItem keyRingItem = player.getInventory().get(InventoryItemType.KEY_RING);
		
		if (keyRingItem == null) {
			keyRingItem = new KeyRingInventoryItem();
			player.getInventory().put(InventoryItemType.KEY_RING, keyRingItem);
		}
		
		KeyRingInventoryItem keyRing = (KeyRingInventoryItem)player.getInventory().get(InventoryItemType.KEY_RING);
		
		CollectibleKey collectibleKey = (CollectibleKey)piece;
		keyRing.addKey(collectibleKey.getKey());
		
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}
	
}
