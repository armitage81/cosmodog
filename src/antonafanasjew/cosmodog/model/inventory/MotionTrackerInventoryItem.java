package antonafanasjew.cosmodog.model.inventory;

public class MotionTrackerInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	public MotionTrackerInventoryItem() {
		super(InventoryItemType.MOTION_TRACKER);
	}
	
	@Override
	public String description() {
		return "The motion tracker indicates the enemies' location during the night even if you cannot see them. " +
				"Additionally, it shows all enemies on the map.";
	}
	
}
