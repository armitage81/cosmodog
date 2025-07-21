package antonafanasjew.cosmodog.model.inventory;

public class FlashlightInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	public FlashlightInventoryItem() {
		super(InventoryItemType.FLASHLIGHT);
	}
	
	@Override
	public String description() {
		return "This flashlight chases away the darkness of the night. " +
				"Use it to identify enemies and items from a safe distance. " +
				"Batteries are charged during the day.";
	}
	
}
