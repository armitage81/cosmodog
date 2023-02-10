package antonafanasjew.cosmodog.model.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DebuggerInventoryItem extends InventoryItem {

	public static class PlayerPosition implements Serializable {

		private static final long serialVersionUID = -1587744329942130741L;
		
		public int x;
		public int y;
		
		public static PlayerPosition of(int x, int y) {
			PlayerPosition playerPosition = new PlayerPosition();
			playerPosition.x = x;
			playerPosition.y = y;
			return playerPosition;
		}
	}
	
	private static final long serialVersionUID = 1L;

	private List<PlayerPosition> monolithPositions = new ArrayList<>();
	private int currentMonolithPositionIndex = 0;
	
	private List<PlayerPosition> cutscenePositions = new ArrayList<>();
	private int currentCutscenePositionIndex = 0;
	
	private boolean positionDisplayed = false;
	
	private int currentCollectibleTypeIndex = 0;
	
	public DebuggerInventoryItem() {
		super(InventoryItemType.DEBUGGER);
		monolithPositions.add(PlayerPosition.of(53, 29));
		monolithPositions.add(PlayerPosition.of(140, 14));
		monolithPositions.add(PlayerPosition.of(129, 34));
		monolithPositions.add(PlayerPosition.of(299, 22));
		monolithPositions.add(PlayerPosition.of(362, 46));
		monolithPositions.add(PlayerPosition.of(180, 57));
		monolithPositions.add(PlayerPosition.of(395, 69));
		monolithPositions.add(PlayerPosition.of(331, 124));
		monolithPositions.add(PlayerPosition.of(242, 100));
		monolithPositions.add(PlayerPosition.of(95, 126));
		monolithPositions.add(PlayerPosition.of(232, 132));
		monolithPositions.add(PlayerPosition.of(379, 155));
		monolithPositions.add(PlayerPosition.of(239, 159));
		monolithPositions.add(PlayerPosition.of(42, 173));
		monolithPositions.add(PlayerPosition.of(124, 219));
		monolithPositions.add(PlayerPosition.of(188, 195));
		monolithPositions.add(PlayerPosition.of(219, 214));
		monolithPositions.add(PlayerPosition.of(268, 216));
		monolithPositions.add(PlayerPosition.of(12, 263));
		monolithPositions.add(PlayerPosition.of(29, 283));
		monolithPositions.add(PlayerPosition.of(136, 278));
		monolithPositions.add(PlayerPosition.of(284, 274));
		monolithPositions.add(PlayerPosition.of(317, 271));
		monolithPositions.add(PlayerPosition.of(385, 288));
		monolithPositions.add(PlayerPosition.of(170, 316));
		monolithPositions.add(PlayerPosition.of(140, 306));
		monolithPositions.add(PlayerPosition.of(105, 285));
		monolithPositions.add(PlayerPosition.of(270, 376));
		monolithPositions.add(PlayerPosition.of(55, 363));
		monolithPositions.add(PlayerPosition.of(6, 370));
		monolithPositions.add(PlayerPosition.of(276, 395));
		monolithPositions.add(PlayerPosition.of(364, 389));
		
		cutscenePositions.add(PlayerPosition.of(7, 9));
		cutscenePositions.add(PlayerPosition.of(7, 21));
		cutscenePositions.add(PlayerPosition.of(7, 34));
		cutscenePositions.add(PlayerPosition.of(45, 58));
		cutscenePositions.add(PlayerPosition.of(238, 238));
		cutscenePositions.add(PlayerPosition.of(211, 249));
		cutscenePositions.add(PlayerPosition.of(240, 186));
	}
	
	public PlayerPosition nextMonolithPosition() {
		PlayerPosition pos = monolithPositions.get(currentMonolithPositionIndex);
		currentMonolithPositionIndex = currentMonolithPositionIndex >= monolithPositions.size() - 1 ? 0 : currentMonolithPositionIndex + 1; 
		return pos;
	}
	
	public PlayerPosition nextCutscenePosition() {
		PlayerPosition pos = cutscenePositions.get(currentCutscenePositionIndex);
		currentCutscenePositionIndex = currentCutscenePositionIndex >= cutscenePositions.size() - 1 ? 0 : currentCutscenePositionIndex + 1; 
		return pos;
	}
 	
	@Override
	public String description() {
		return "Debugger.";
	}
	
	public boolean isPositionDisplayed() {
		return positionDisplayed;
	}
	
	public void setPositionDisplayed(boolean positionDisplayed) {
		this.positionDisplayed = positionDisplayed;
	}
	
	public int nextCollectibleTypeIndex() {
		int index = currentCollectibleTypeIndex;
		currentCollectibleTypeIndex = (currentCollectibleTypeIndex + 1) % 10;
		return index;
	}

}
