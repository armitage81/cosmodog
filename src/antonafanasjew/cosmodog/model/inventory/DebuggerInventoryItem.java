package antonafanasjew.cosmodog.model.inventory;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DebuggerInventoryItem extends InventoryItem {

	private static final long serialVersionUID = 1L;

	private List<Position> positions = new ArrayList<>();
	private int currentPositionIndex = 0;
	
	private boolean positionDisplayed = false;
	
	private int currentCollectibleTypeIndex = 0;

	public DebuggerInventoryItem(String positionsString) {
		super(InventoryItemType.DEBUGGER);
		Arrays.stream(positionsString.split(";")).map(s -> {
			String[] parts = s.split("/");
			int x = Integer.parseInt(parts[0]);
			int y = Integer.parseInt(parts[1]);
			MapType mapType = MapType.MAIN;
			if (parts.length > 2) {
				mapType = MapType.valueOf(parts[2]);
			}
			return Position.fromCoordinates(x, y, mapType);
		}).forEach(p -> positions.add(p));
	}

	public DebuggerInventoryItem(List<Position> positions) {
		super(InventoryItemType.DEBUGGER);
		this.positions.addAll(positions);
	}

	public DebuggerInventoryItem() {
		super(InventoryItemType.DEBUGGER);

		positions.add(Position.fromCoordinates(53, 29, MapType.MAIN));
		positions.add(Position.fromCoordinates(140, 14, MapType.MAIN));
		positions.add(Position.fromCoordinates(129, 34, MapType.MAIN));
		positions.add(Position.fromCoordinates(299, 22, MapType.MAIN));
		positions.add(Position.fromCoordinates(362, 46, MapType.MAIN));
		positions.add(Position.fromCoordinates(180, 57, MapType.MAIN));
		positions.add(Position.fromCoordinates(395, 69, MapType.MAIN));
		positions.add(Position.fromCoordinates(331, 124, MapType.MAIN));
		positions.add(Position.fromCoordinates(242, 100, MapType.MAIN));
		positions.add(Position.fromCoordinates(96, 145, MapType.MAIN));
		positions.add(Position.fromCoordinates(232, 132, MapType.MAIN));
		positions.add(Position.fromCoordinates(379, 155, MapType.MAIN));
		positions.add(Position.fromCoordinates(391, 92 , MapType.MAIN));
		positions.add(Position.fromCoordinates(42, 173, MapType.MAIN));
		positions.add(Position.fromCoordinates(124, 219, MapType.MAIN));
		positions.add(Position.fromCoordinates(188, 195, MapType.MAIN));
		positions.add(Position.fromCoordinates(219, 214, MapType.MAIN));
		positions.add(Position.fromCoordinates(268, 216, MapType.MAIN));
		positions.add(Position.fromCoordinates(12, 263, MapType.MAIN));
		positions.add(Position.fromCoordinates(29, 283, MapType.MAIN));
		positions.add(Position.fromCoordinates(136, 278, MapType.MAIN));
		positions.add(Position.fromCoordinates(284, 274, MapType.MAIN));
		positions.add(Position.fromCoordinates(317, 271, MapType.MAIN));
		positions.add(Position.fromCoordinates(385, 288, MapType.MAIN));
		positions.add(Position.fromCoordinates(170, 316, MapType.MAIN));
		positions.add(Position.fromCoordinates(140, 306, MapType.MAIN));
		positions.add(Position.fromCoordinates(105, 285, MapType.MAIN));
		positions.add(Position.fromCoordinates(270, 376, MapType.MAIN));
		positions.add(Position.fromCoordinates(55, 363, MapType.MAIN));
		positions.add(Position.fromCoordinates(6, 370, MapType.MAIN));
		positions.add(Position.fromCoordinates(276, 395, MapType.MAIN));
		positions.add(Position.fromCoordinates(364, 389, MapType.MAIN));
		positions.add(Position.fromCoordinates(191, 171, MapType.SPACE));

		positions.add(Position.fromCoordinates(7, 9, MapType.MAIN));
		positions.add(Position.fromCoordinates(7, 21, MapType.MAIN));
		positions.add(Position.fromCoordinates(45, 58, MapType.MAIN));
		positions.add(Position.fromCoordinates(238, 238, MapType.MAIN));
		positions.add(Position.fromCoordinates(211, 249, MapType.MAIN));
	}

	public Position firstPosition() {
		Position pos = positions.getFirst();
		currentPositionIndex = 1;
		return pos;
	}

	public Position nextPosition() {
		Position pos = positions.get(currentPositionIndex);
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
