package antonafanasjew.cosmodog.model.inventory;

public class RadioactiveSuitInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	public RadioactiveSuitInventoryItem() {
		super(InventoryItemType.RADIOACTIVESUIT);
	}
	
	@Override
	public String description() {
		return "This protective suit can be worn under the jacket. It suppresses radioactive damage to your body.";
	}
	
}
