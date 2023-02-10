package antonafanasjew.cosmodog.model.inventory;

public class WeaponFirmwareUpgradeInventoryItem extends InventoryItem {

	private static final long serialVersionUID = 5769162124739840279L;

	private static final String DESCRIPTION = "This is the newest weapon firmware upgrade. The  carrier port is a bit rusty but still functioning. According to the label, the software version is an early alpha. The modification triples the damage of all your weapons.";
	
	public WeaponFirmwareUpgradeInventoryItem() {
		super(InventoryItemType.WEAPON_FIRMWARE_UPGRADE);
	}

	@Override
	public String description() {
		return DESCRIPTION;
	}

	
}
