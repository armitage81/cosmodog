package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;

public class PlatformInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		PlatformInventoryItem platformInventoryItem = new PlatformInventoryItem((Platform)piece);
		player.getInventory().put(InventoryItemType.PLATFORM, platformInventoryItem);
		
		//Entering a platform must reset thirst and hunger. This makes sense since the metaphor is that the platform has infinite supplies.
		player.setFood(player.getCurrentMaxFood());
		player.setLifeLentForHunger(0);
		player.setWater(player.getCurrentMaxWater());
		player.setLifeLentForThirst(0);
	}

	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_CAR_DOOR;
	}
	
}
