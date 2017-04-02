package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SoftwareInventoryItem;

public class SoftwareInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		InventoryItem item = player.getInventory().get(InventoryItemType.SOFTWARE);

		if (item != null) {
			SoftwareInventoryItem cosmodogCollectionItem = (SoftwareInventoryItem) item;
			cosmodogCollectionItem.increaseNumber();
		} else {
			SoftwareInventoryItem cosmodogCollectionItem = new SoftwareInventoryItem();
			cosmodogCollectionItem.setNumber(1);
			player.getInventory().put(InventoryItemType.SOFTWARE, cosmodogCollectionItem);
		}

	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}
	
}
