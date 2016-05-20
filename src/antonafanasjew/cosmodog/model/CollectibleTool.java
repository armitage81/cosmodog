package antonafanasjew.cosmodog.model;

public class CollectibleTool extends Collectible {

	private static final long serialVersionUID = 5790400319374924936L;

	public static enum ToolType {
		boat,
		dynamite,
		geigerzaehler
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
