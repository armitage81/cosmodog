package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DynamiteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.util.NarrativeSequenceUtils;
import antonafanasjew.cosmodog.util.NotificationUtils;

public class DynamiteInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundDynamite()), true, false);
		player.getInventory().put(InventoryItemType.DYNAMITE, new DynamiteInventoryItem());
	}

}
