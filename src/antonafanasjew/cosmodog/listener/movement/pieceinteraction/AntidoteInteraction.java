package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.AntidoteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;

public class AntidoteInteraction extends ToolInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		player.getInventory().put(InventoryItemType.ANTIDOTE, new AntidoteInventoryItem());
		player.decontaminate();
	}

	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}

	@Override
	protected String text() {
		return "You found the antidote. Poison is not a threat anymore.";
	}
}
