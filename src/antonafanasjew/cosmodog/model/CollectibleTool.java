package antonafanasjew.cosmodog.model;

public class CollectibleTool extends Collectible {

	private static final long serialVersionUID = 5790400319374924936L;

	public static final String ITEM_TYPE_BOAT = "item.boat";
	public static final String ITEM_TYPE_DYNAMITE = "item.dynamite";
	
	public static enum ToolType {
		boat,
		dynamite
	}
	
	private ToolType toolType;
	
	public CollectibleTool(ToolType toolType) {
		super(Collectible.CollectibleType.TOOL);
		this.toolType = toolType;
		
	}

	public ToolType getToolType() {
		return toolType;
	}

}
