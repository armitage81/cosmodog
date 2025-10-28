package antonafanasjew.cosmodog.model.inventory;

import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DebuggerInventoryItem extends InventoryItem {

	private static final long serialVersionUID = 1L;

	private List<Position> positions = new ArrayList<>();
	private int currentPositionIndex = 0;
	
	private boolean positionDisplayed = false;
	
	private int currentCollectibleTypeIndex = 0;

	public DebuggerInventoryItem(String positionsString) {
		super(InventoryItemType.DEBUGGER);
		Map<String, MapDescriptor> mapDescriptors = ApplicationContextUtils.mapDescriptors();
		Arrays.stream(positionsString.split(";")).map(s -> {
			String[] parts = s.split("/");
			int x = Integer.parseInt(parts[0]);
			int y = Integer.parseInt(parts[1]);
			MapDescriptor mapDescriptor = mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN);
			if (parts.length > 2) {
				mapDescriptor = mapDescriptors.get(parts[2]);
			}
			return Position.fromCoordinates(x, y, mapDescriptor);
		}).forEach(p -> positions.add(p));
	}

	public DebuggerInventoryItem(List<Position> positions) {
		super(InventoryItemType.DEBUGGER);
		this.positions.addAll(positions);
	}

	public DebuggerInventoryItem() {
		super(InventoryItemType.DEBUGGER);
		Map<String, MapDescriptor> mapDescriptors = ApplicationContextUtils.mapDescriptors();
		positions.add(Position.fromCoordinates(53, 29, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(140, 14, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(129, 34, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(299, 22, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(362, 46, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(180, 57, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(395, 69, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(331, 124, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(242, 100, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(96, 145, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(232, 132, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(379, 155, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(391, 92 , mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(42, 173, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(124, 219, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(188, 195, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(219, 214, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(268, 216, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(12, 263, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(29, 283, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(136, 278, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(284, 274, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(317, 271, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(385, 288, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(170, 316, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(140, 306, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(105, 285, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(270, 376, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(55, 363, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(6, 370, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(276, 395, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(364, 389, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(191, 171, mapDescriptors.get(MapDescriptor.MAP_NAME_SPACE)));

		positions.add(Position.fromCoordinates(7, 9, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(7, 21, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(45, 58, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(238, 238, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		positions.add(Position.fromCoordinates(211, 249, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
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
