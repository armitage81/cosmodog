package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.MotionTrackerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.RadioactiveSuitInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class MotionTrackerInteraction extends ToolInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		player.getInventory().put(InventoryItemType.MOTION_TRACKER, new MotionTrackerInventoryItem());
	}
	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}

	@Override
	protected String text() {
		Player player = ApplicationContextUtils.getPlayer();
		return "You found the motion tracker. It indicates the enemies' locations at night even if you do not see them and also shows them on the map.";
	}
	
}
