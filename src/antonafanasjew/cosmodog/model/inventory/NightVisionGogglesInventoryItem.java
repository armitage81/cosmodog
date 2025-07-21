package antonafanasjew.cosmodog.model.inventory;

public class NightVisionGogglesInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	public NightVisionGogglesInventoryItem() {
		super(InventoryItemType.NIGHT_VISION_GOGGLES);
	}
	
	@Override
	public String description() {
		return "The night vision goggles replace the flashlight. They allow you to see all items and all enemies at night... " +
				"and make you feel a bit like a renegade cyber ninja.";
	}
	
}
