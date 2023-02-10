package antonafanasjew.cosmodog.model.inventory;

public class ArcheologistsJournalInventoryItem extends InventoryItem {

	private static final long serialVersionUID = 5769162124739840279L;

	private static final String DESCRIPTION = "Turns out this \"journal\" is merely an old tourist guide brochure for Lydport and surrounding area "
			+ "that was wrapped in a fancy looking leather cover to appear more authentic. "
			+ "Apparently, the book has been used as a requisite for a movie production. "
			+ "Nevertheless, it describes all noteworthy sights and points of interest in the area and can be used to gain knowledge about the valley. "
			+ "(Now your map shows the amount of infobits in each charted part of it.)";
	
	public ArcheologistsJournalInventoryItem() {
		super(InventoryItemType.ARCHEOLOGISTS_JOURNAL);
	}

	@Override
	public String description() {
		return DESCRIPTION;
	}

	
}
