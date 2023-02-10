package antonafanasjew.cosmodog.model.inventory;

public class MineDetectorInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	public static final int DETECTION_DISTANCE = 6;
	
	public MineDetectorInventoryItem() {
		super(InventoryItemType.MINEDETECTOR);
	}
	
	@Override
	public String description() {
		return "The mine detector will detect land mines when you approach them.";
	}

}
