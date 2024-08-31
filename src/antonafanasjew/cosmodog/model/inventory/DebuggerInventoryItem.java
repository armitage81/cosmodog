package antonafanasjew.cosmodog.model.inventory;

import antonafanasjew.cosmodog.topology.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

	private List<PlayerPosition> positions = new ArrayList<>();
	private int currentPositionIndex = 0;
	
	private boolean positionDisplayed = false;
	
	private int currentCollectibleTypeIndex = 0;

	public DebuggerInventoryItem(String positionsString) {
		super(InventoryItemType.DEBUGGER);
		Arrays.stream(positionsString.split(";")).map(s -> {
			String[] parts = s.split("/");
			int x = Integer.parseInt(parts[0]);
			int y = Integer.parseInt(parts[1]);
			return PlayerPosition.of(x, y);
		}).forEach(p -> positions.add(p));
	}

	public DebuggerInventoryItem(List<PlayerPosition> positions) {
		super(InventoryItemType.DEBUGGER);
		this.positions.addAll(positions);
	}

	public DebuggerInventoryItem() {
		super(InventoryItemType.DEBUGGER);
		positions.add(PlayerPosition.of(53, 29));
		positions.add(PlayerPosition.of(140, 14));
		positions.add(PlayerPosition.of(129, 34));
		positions.add(PlayerPosition.of(299, 22));
		positions.add(PlayerPosition.of(362, 46));
		positions.add(PlayerPosition.of(180, 57));
		positions.add(PlayerPosition.of(395, 69));
		positions.add(PlayerPosition.of(331, 124));
		positions.add(PlayerPosition.of(242, 100));
		positions.add(PlayerPosition.of(95, 144));
		positions.add(PlayerPosition.of(232, 132));
		positions.add(PlayerPosition.of(379, 155));
		positions.add(PlayerPosition.of(239, 159));
		positions.add(PlayerPosition.of(42, 173));
		positions.add(PlayerPosition.of(124, 219));
		positions.add(PlayerPosition.of(188, 195));
		positions.add(PlayerPosition.of(219, 214));
		positions.add(PlayerPosition.of(268, 216));
		positions.add(PlayerPosition.of(12, 263));
		positions.add(PlayerPosition.of(29, 283));
		positions.add(PlayerPosition.of(136, 278));
		positions.add(PlayerPosition.of(284, 274));
		positions.add(PlayerPosition.of(317, 271));
		positions.add(PlayerPosition.of(385, 288));
		positions.add(PlayerPosition.of(170, 316));
		positions.add(PlayerPosition.of(140, 306));
		positions.add(PlayerPosition.of(105, 285));
		positions.add(PlayerPosition.of(270, 376));
		positions.add(PlayerPosition.of(55, 363));
		positions.add(PlayerPosition.of(6, 370));
		positions.add(PlayerPosition.of(276, 395));
		positions.add(PlayerPosition.of(364, 389));

		positions.add(PlayerPosition.of(7, 9));
		positions.add(PlayerPosition.of(7, 21));
		positions.add(PlayerPosition.of(45, 58));
		positions.add(PlayerPosition.of(238, 238));
		positions.add(PlayerPosition.of(211, 249));
	}
	
	public PlayerPosition nextPosition() {
		PlayerPosition pos = positions.get(currentPositionIndex);
		currentPositionIndex = currentPositionIndex >= positions.size() - 1 ? 0 : currentPositionIndex + 1;
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
