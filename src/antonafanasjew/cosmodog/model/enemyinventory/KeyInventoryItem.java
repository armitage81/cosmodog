package antonafanasjew.cosmodog.model.enemyinventory;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.dynamicpieces.Door;

public class KeyInventoryItem extends EnemyInventoryItem {

    private final Door.DoorType doorType;

    public KeyInventoryItem(Door.DoorType doorType) {
        super(EnemyInventoryItemType.KEY);
        this.doorType = doorType;
    }

    public Door.DoorType getDoorType() {
        return doorType;
    }
}
