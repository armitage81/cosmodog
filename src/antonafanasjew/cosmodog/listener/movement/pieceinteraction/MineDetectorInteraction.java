package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.MineDeactivationCodesInventoryItem;
import antonafanasjew.cosmodog.model.inventory.MineDetectorInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class MineDetectorInteraction extends ToolInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		Inventory inventory = player.getInventory();
		
		MineDetectorInventoryItem mineDetectorInventoryItem = (MineDetectorInventoryItem)inventory.get(InventoryItemType.MINEDETECTOR);
		
		if (mineDetectorInventoryItem == null) {
			player.getInventory().put(InventoryItemType.MINEDETECTOR, new MineDetectorInventoryItem());
		} else {
			player.getInventory().put(InventoryItemType.MINEDEACTIVATIONCODES, new MineDeactivationCodesInventoryItem());
		}
		
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}
	
	@Override
	protected String text() {
		
		Player player = ApplicationContextUtils.getPlayer();
		
		Inventory inventory = player.getInventory();
		
		MineDetectorInventoryItem mineDetectorInventoryItem = (MineDetectorInventoryItem)inventory.get(InventoryItemType.MINEDETECTOR);
		
		if (mineDetectorInventoryItem == null) {
			return "You found a mine detector. It will detect nearby mines. Avoid stepping on them.";
		} else {
			return "You found mine deactivation codes. Use them to disarm mines via special terminals.";
		}
		
	}
	
}
