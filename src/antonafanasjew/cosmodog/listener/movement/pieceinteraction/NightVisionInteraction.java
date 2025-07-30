package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class NightVisionInteraction extends ToolInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		FlashlightInventoryItem flashlight = (FlashlightInventoryItem) player.getInventory().get(InventoryItemType.FLASHLIGHT);
		if (flashlight == null) {
			player.getInventory().put(InventoryItemType.FLASHLIGHT, new FlashlightInventoryItem());
		} else {
			player.getInventory().put(InventoryItemType.NIGHT_VISION_GOGGLES, new NightVisionGogglesInventoryItem());
		}
	}
	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}

	@Override
	protected String text() {
		Player player = ApplicationContextUtils.getPlayer();

		FlashlightInventoryItem flashlight = (FlashlightInventoryItem) player.getInventory().get(InventoryItemType.FLASHLIGHT);
		if (flashlight == null) {
			return "You found a flashlight. It will increase your sight at night.";
		} else {
			return "You found the night vision goggles. They will remove all sight restrictions at night.";
		}
	}
	
}
