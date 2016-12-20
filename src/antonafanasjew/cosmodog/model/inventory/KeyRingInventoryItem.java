package antonafanasjew.cosmodog.model.inventory;

import java.util.List;
import java.util.Map;

import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.upgrades.Key;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class KeyRingInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	private Map<DoorType, Key> keys = Maps.newHashMap();
	
	private List<DoorType> doorTypesOrder = Lists.newArrayList(
			
			DoorType.redKeycardDoor,
			DoorType.lilaKeycardDoor,
			DoorType.blueKeycardDoor,
			DoorType.cyanKeycardDoor,
			DoorType.greenKeycardDoor,
			DoorType.yellowKeycardDoor,
			DoorType.brownKeycardDoor,
			DoorType.purpleKeycardDoor,
			DoorType.darkblueKeycardDoor,
			DoorType.whiteKeycardDoor,
			DoorType.yellowKeyDoor,
			DoorType.greenKeyDoor,
			DoorType.blueKeyDoor,
			DoorType.redKeyDoor,
			DoorType.blackKeyDoor
	);
		
	public KeyRingInventoryItem() {
		super(InventoryItemType.KEY_RING);
	}
	
	public void addKey(Key key) {
		this.keys.put(key.getDoorType(), key);
	}
	
	public Map<DoorType, Key> getKeysCopy() {
		return Maps.newHashMap(keys);
	}

	@Override
	public String description() {
		return "This is the collection of all found keys and key cards.";
	}

	public List<DoorType> getDoorTypesOrder() {
		return doorTypesOrder;
	}

}
