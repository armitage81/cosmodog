package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.RadioactiveSuitInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class GeigerZaehlerInteraction extends ToolInteraction {

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

	@Override
	protected String text() {
		Player player = ApplicationContextUtils.getPlayer();
		
		GeigerZaehlerInventoryItem geigerCounter = (GeigerZaehlerInventoryItem)player.getInventory().get(InventoryItemType.GEIGERZAEHLER);
		if (geigerCounter == null) {
			return "You found a Geiger counter. It indicates radiation. The 'RAD' value shows the contaminated adjacent tiles including your own position.";
		} else {
			return "You found the radiation suit. It will protect you from radioactive rays.";
		}
	}
	
}
