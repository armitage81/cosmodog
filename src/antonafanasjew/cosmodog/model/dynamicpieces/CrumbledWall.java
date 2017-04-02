package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Represents a dynamic tile that is a crumbled wall field that can be destroyed by the player if he has dynamite.
 */
public class CrumbledWall extends DynamicPiece {

	private static final long serialVersionUID = 5708806596326635656L;

	public static final short SHAPE_MONTAIN = 0;
	public static final short SHAPE_ALIEN_BASE = 1;
	
	public static final short STATE_WHOLE = 0;
	public static final short STATE_DESTROYED = 1;
	
	
	private short state = STATE_WHOLE;
	private short shapeNumber;
	
	public short getState() {
		return state;
	}
	
	public boolean isDestroyed() {
		return state == STATE_DESTROYED;
	}
	
	public static CrumbledWall create(int x, int y, short shapeNumber) {
		CrumbledWall wall = new CrumbledWall();
		wall.setPositionX(x);
		wall.setPositionY(y);
		wall.shapeNumber = shapeNumber;
		return wall;
	}
	
	public String animationSuffixFromState() {
		return String.valueOf(state);
	}

	public short getShapeNumber() {
		return shapeNumber;
	}

	@Override
	public void interact() {
		Player player = ApplicationContextUtils.getPlayer();
		Inventory inventory = player.getInventory();
		InventoryItem dynamite = inventory.get(InventoryItemType.DYNAMITE);
		if (dynamite != null) {
			if (state < STATE_DESTROYED) {
				state++;
			}
		}
	}

	@Override
	public boolean wrapsCollectible() {
		return state != STATE_DESTROYED;
	}

}
