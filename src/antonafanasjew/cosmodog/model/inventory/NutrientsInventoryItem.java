package antonafanasjew.cosmodog.model.inventory;

import java.io.Serial;

public class NutrientsInventoryItem extends InventoryItem {

	@Serial
	private static final long serialVersionUID = 5769162124739840279L;

	private static final String DESCRIPTION = "These pills are used by astronauts who fly long distances. They provide all nutrition the human body needs. Your food consumption is reduced by half.";

	public NutrientsInventoryItem() {
		super(InventoryItemType.NUTRIENTS);
	}

	@Override
	public String description() {
		return DESCRIPTION;
	}

	
}
