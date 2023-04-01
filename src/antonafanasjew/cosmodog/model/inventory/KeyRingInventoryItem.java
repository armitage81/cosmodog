package antonafanasjew.cosmodog.model.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.upgrades.Key;


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
		
		List<String> keyDescriptions = new ArrayList<>();
		for (DoorType doorType : keys.keySet()) {
			keyDescriptions.add(" - " + doorType.getKeyDescription());
		}
		
		Collections.sort(keyDescriptions);
		
		String keyDescriptionsText = Joiner.on("<br>").join(keyDescriptions);
		
		return "Your keys:" + "<p>" + keyDescriptionsText;
	}

	public List<DoorType> getDoorTypesOrder() {
		return doorTypesOrder;
	}

}
