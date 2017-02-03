package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.KeyRingInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Key;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class Door extends DynamicPiece {


	private static final long serialVersionUID = -3679262029935812988L;

	public static enum DoorAppearanceType {
		menesian,
		snowLab,
		defaultBuilding,
		glasHouse;
	}
	
	public static enum DoorType {
		redKeycardDoor,
		lilaKeycardDoor,
		blueKeycardDoor,
		cyanKeycardDoor,
		greenKeycardDoor,
		yellowKeycardDoor,
		brownKeycardDoor,
		purpleKeycardDoor,
		darkblueKeycardDoor,
		whiteKeycardDoor,
		yellowKeyDoor,
		greenKeyDoor,
		blueKeyDoor,
		redKeyDoor,
		blackKeyDoor
		
	}
	
	private DirectionType exitDirectionType;
	private DoorType doorType;
	private DoorAppearanceType doorAppearanceType;
	private boolean opened;

	public Door(DirectionType exitDirectionType, DoorType doorType, DoorAppearanceType doorAppearanceType) {
		this.setExitDirectionType(exitDirectionType);
		this.doorType = doorType;
		this.doorAppearanceType = doorAppearanceType;
	}

	@Override
	public void interact() {
		if (!opened) {
			Player player = ApplicationContextUtils.getPlayer();
			Inventory inventory = player.getInventory();
			KeyRingInventoryItem keyRing = (KeyRingInventoryItem)inventory.get(InventoryItemType.KEY_RING);
			Key keyForDoor = keyRing.getKeysCopy().get(this.doorType);
			if (keyForDoor != null) {
				opened = true;
			}
		}
	}

	@Override
	public boolean wrapsCollectible() {
		return false;
	}

	
	
	public DoorType getDoorType() {
		return doorType;
	}

	public void setDoorType(DoorType doorType) {
		this.doorType = doorType;
	}

	public DirectionType getExitDirectionType() {
		return exitDirectionType;
	}

	public void setExitDirectionType(DirectionType exitDirectionType) {
		this.exitDirectionType = exitDirectionType;
	}
	
	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public DoorAppearanceType getDoorAppearanceType() {
		return doorAppearanceType;
	}


}
