package antonafanasjew.cosmodog.model.inventory;

public class MineDetectorInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	private static final int INITIAL_DETECTION_DISTANCE = 2;
	
	private int detectionDistance = INITIAL_DETECTION_DISTANCE;
	
	public MineDetectorInventoryItem() {
		super(InventoryItemType.MINEDETECTOR);
	}
	
	@Override
	public String description() {
		return "The mine detector cannot disarm land mines but it will detect them and make them visible as you approach them.";
	}

	public int getDetectionDistance() {
		return detectionDistance;
	}

	public void increaseDetectionDistance() {
		this.detectionDistance++;
	}
	
}
