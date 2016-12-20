package antonafanasjew.cosmodog.model.upgrades;

import java.io.Serializable;

import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;

public class Key implements Serializable {

	private static final long serialVersionUID = -7362438886018464478L;
	private DoorType doorType;

	public DoorType getDoorType() {
		return doorType;
	}

	public void setDoorType(DoorType doorType) {
		this.doorType = doorType;
	}
	
}
