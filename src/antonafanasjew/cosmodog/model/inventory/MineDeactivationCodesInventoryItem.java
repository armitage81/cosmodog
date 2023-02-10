package antonafanasjew.cosmodog.model.inventory;

public class MineDeactivationCodesInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	public MineDeactivationCodesInventoryItem() {
		super(InventoryItemType.MINEDEACTIVATIONCODES);
	}
	
	@Override
	public String description() {
		return "Use these codes to disarm mines via mine deactivation terminals.";
	}

}
