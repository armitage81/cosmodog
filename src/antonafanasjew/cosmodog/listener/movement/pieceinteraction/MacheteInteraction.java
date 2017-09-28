package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.MacheteInventoryItem;

public class MacheteInteraction extends ToolInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		player.getInventory().put(InventoryItemType.MACHETE, new MacheteInventoryItem());		
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}

	@Override
	protected String text() {
		return "You found a machete. It can be used to destroy the bamboo stems.";
	}
	
}
