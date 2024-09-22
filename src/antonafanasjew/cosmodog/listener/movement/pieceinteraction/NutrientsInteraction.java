package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.NutrientsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.WeaponFirmwareUpgradeInventoryItem;

public class NutrientsInteraction extends ToolInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		player.getInventory().put(InventoryItemType.NUTRIENTS, new NutrientsInventoryItem());
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}

	@Override
	protected String text() {
		return "You found the nutrient pills. Your food consumption is reduced by half.";
	}
	
}
