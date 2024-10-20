package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class BinocularsInteraction extends ToolInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
		Cam cam = cosmodogGame.getCam();
		cam.focusOnPiece(0, 0, player);
		cam.zoomOut();
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}

	@Override
	protected String text() {
		return "You found the binoculars. You can see distant things now. Press [Z] to zoom in and out.";
	}
	
	
	
}
