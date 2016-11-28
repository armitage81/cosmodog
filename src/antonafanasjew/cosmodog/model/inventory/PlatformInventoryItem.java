package antonafanasjew.cosmodog.model.inventory;

import antonafanasjew.cosmodog.model.actors.Platform;

/**
 * Wraps a collectible vehicle as an inventory item.
 */
public class PlatformInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	private Platform platform;
	private boolean exiting;

	public PlatformInventoryItem(Platform platform) {
		super(InventoryItemType.PLATFORM);
		this.platform = platform;
	}
	
	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public boolean isExiting() {
		return exiting;
	}

	public void setExiting(boolean exiting) {
		this.exiting = exiting;
	}
	
}
