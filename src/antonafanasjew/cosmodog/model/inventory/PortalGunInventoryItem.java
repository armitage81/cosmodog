package antonafanasjew.cosmodog.model.inventory;

import java.io.Serial;

public class PortalGunInventoryItem extends InventoryItem {

	@Serial
	private static final long serialVersionUID = 5769162124739840279L;

	private static final String DESCRIPTION = "The portal gun is an experimental tool able to connect two different points in space. " +
			"Unfortunately, it only works on magnetic pillars in laboratory environments. Press [SPACE] to attach a portal to a pillar in front of you. " +
			"Create a second portal to establish an interdimensional tunnel between the two pillars.";

	public PortalGunInventoryItem() {
		super(InventoryItemType.PORTAL_GUN);
	}

	@Override
	public String description() {
		return DESCRIPTION;
	}

	
}
