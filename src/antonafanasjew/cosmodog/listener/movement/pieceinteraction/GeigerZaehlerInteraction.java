package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.RadioactiveSuitInventoryItem;

public class GeigerZaehlerInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		GeigerZaehlerInventoryItem geigerCounter = (GeigerZaehlerInventoryItem)player.getInventory().get(InventoryItemType.GEIGERZAEHLER);
		if (geigerCounter == null) {
			player.getInventory().put(InventoryItemType.GEIGERZAEHLER, new GeigerZaehlerInventoryItem());
		} else {
			player.getInventory().put(InventoryItemType.RADIOACTIVESUIT, new RadioactiveSuitInventoryItem());
		}
	}
	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}

}
