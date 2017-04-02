package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.util.NarrativeSequenceUtils;
import antonafanasjew.cosmodog.util.NotificationUtils;

public class BoatInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundBoat()), true, false);
		player.getInventory().put(InventoryItemType.BOAT, new BoatInventoryItem());
		//PiecesUtils.removeAllCollectibleItems(CollectibleItem.ITEM_TYPE_BOAT, cosmodogMap);		
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}
	
}
