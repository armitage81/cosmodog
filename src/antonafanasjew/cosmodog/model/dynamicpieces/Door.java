package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
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
		Menesian,
		SnowLab,
		DefaultBuilding,
		GlasHouse,
		AlienDoor;
	}
	
	public static enum DoorType {
		redKeycardDoor("red keycard"),
		lilaKeycardDoor("purple keycard"),
		blueKeycardDoor("blue keycard"),
		cyanKeycardDoor("cyan keycard"),
		greenKeycardDoor("green keycard"),
		yellowKeycardDoor("yellow keycard"),
		brownKeycardDoor("brown keycard"),
		purpleKeycardDoor("crimson keycard"),
		darkblueKeycardDoor("dark blue keycard"),
		whiteKeycardDoor("white keycard"),
		yellowKeyDoor("yellow key"),
		greenKeyDoor("green key"),
		blueKeyDoor("blue key"),
		redKeyDoor("red key"),
		blackKeyDoor("black key");
		
		private String keyDescription;
		
		private DoorType(String keyDescription) {
			this.keyDescription = keyDescription;
		}
		
		public String getKeyDescription() {
			return keyDescription;
		}
		
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
			if (keyRing != null) {
				Key keyForDoor = keyRing.getKeysCopy().get(this.doorType);
				if (keyForDoor != null) {
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
	
	public boolean closed() {
		return !isOpened();
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public DoorAppearanceType getDoorAppearanceType() {
		return doorAppearanceType;
	}

	@Override
	public String animationId(boolean bottomNotTop) {
		String animationIdPrefix = "dynamicPiece" + getDoorAppearanceType() + getExitDirectionType().getRepresentation();
		String animationIdInfix = bottomNotTop ? "Bottom" : "Top";
		String animationSuffix = isOpened() ? "Open" : "Closed";
        return animationIdPrefix + animationIdInfix + animationSuffix;
	}
}
