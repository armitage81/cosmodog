package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class AlienBaseBlockade extends DynamicPiece {


	private static final long serialVersionUID = -3679262029935812988L;
	
	private boolean opened;

	@Override
	public void interact() {
		if (!opened) {
			Player player = ApplicationContextUtils.getPlayer();
			Inventory inventory = player.getInventory();
			
			InsightInventoryItem insight = (InsightInventoryItem)inventory.get(InventoryItemType.INSIGHT);
			if (insight != null) {
				if (insight.getNumber() >= Constants.MIN_INSIGHTS_TO_OPEN_ALIEN_BASE) {
					opened = true;
				}
			}
			
			if (!opened) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_LOCKED_ALIEN_DOOR).play();
			} else {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_OPENING_ALIEN_DOOR).play();
			}
			
		}
	}

	public static AlienBaseBlockade create(int x, int y) {
		AlienBaseBlockade retVal = new AlienBaseBlockade();
		retVal.setPosition(Position.fromCoordinates(x, y));
		retVal.opened = false;
		return retVal;
	}
	
	@Override
	public boolean wrapsCollectible() {
		return false;
	}

	public boolean isOpened() {
		return opened;
	}
	
	public boolean closed() {
		return !isOpened();
	}

}
