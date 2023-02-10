package antonafanasjew.cosmodog.model;

public class CollectibleTool extends Collectible {

	private static final long serialVersionUID = 5790400319374924936L;

	public static enum ToolType {
		boat(5000),
		dynamite(5000),
		geigerzaehler(5000),
		supplytracker(5000),
		binoculars(5000),
		jacket(5000),
		antidote(5000),
		minedetector(5000),
		ski(5000),
		pick(5000),
		axe(5000),
		machete(5000),
		archeologistsJournal(5000),
		weaponFirmwareUpgrade(5000);
		
		private int scorePoints;

		private ToolType(int scorePoints) {
			this.scorePoints = scorePoints;
		}
		
		public int getScorePoints() {
			return scorePoints;
		}

	}
	
	
	private ToolType toolType;
	
	public CollectibleTool(ToolType toolType) {
		super(Collectible.CollectibleType.TOOL);
		this.toolType = toolType;
		
	}

	public ToolType getToolType() {
		return toolType;
	}

	@Override
	public String toString() {
		return "Tool: " + toolType.toString();
	}
	
}
