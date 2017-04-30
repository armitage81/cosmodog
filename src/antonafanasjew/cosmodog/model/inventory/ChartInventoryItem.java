package antonafanasjew.cosmodog.model.inventory;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


public class ChartInventoryItem extends CountableInventoryItem {

	private static final long serialVersionUID = -5819631394123309541L;

	public static final int CHART_PIECE_NUMBER_X = 8;
	public static final int CHART_PIECE_NUMBER_Y = 8;
	
	
	private List<Boolean> discoveredCharts = Lists.newArrayList(); //Row after row 
	
	/**
	 * Constructor. Initializes the chart inventory item.
	 */
	public ChartInventoryItem() {
		super(InventoryItemType.CHART);
		
		for (int i = 0; i < CHART_PIECE_NUMBER_X * CHART_PIECE_NUMBER_Y; i++) {
			discoveredCharts.add(Boolean.FALSE);
		}
		
	}

	@Override
	public String description() {
		return "This is a map of the valley in which you crashed. Collect map pieces to chart the whole area. Use the map screen to view charted territory.";
	}

	public boolean pieceIsDiscovered(int x, int y) {
		Preconditions.checkState(x < CHART_PIECE_NUMBER_X);
		Preconditions.checkState(y < CHART_PIECE_NUMBER_Y);
		int index = y * CHART_PIECE_NUMBER_Y + x;
		return discoveredCharts.get(index);
		
	}
	
	public boolean discoverPiece(int x, int y) {
		Preconditions.checkState(x < CHART_PIECE_NUMBER_X);
		Preconditions.checkState(y < CHART_PIECE_NUMBER_Y);
		int index = y * CHART_PIECE_NUMBER_Y + x;
		this.increaseNumber();
		return discoveredCharts.set(index, Boolean.TRUE);
	}
	
}
