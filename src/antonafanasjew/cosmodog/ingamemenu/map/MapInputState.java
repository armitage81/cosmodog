package antonafanasjew.cosmodog.ingamemenu.map;

import antonafanasjew.cosmodog.ingamemenu.InGameMenuInputState;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;

public class MapInputState implements InGameMenuInputState {

	private int selectionX = 0;
	private int selectionY = 0;
	
	public void left() {
		if (selectionX > 0) {
			selectionX -= 1;
		}
	}
	
	public void up() {
		if (selectionY > 0) {
			selectionY -= 1;
		}
	}
	
	public void right() {
		if (selectionX < ChartInventoryItem.CHART_PIECE_NUMBER_X - 1) {
			selectionX += 1;
		}
	}
	
	public void down() {
		if (selectionY < ChartInventoryItem.CHART_PIECE_NUMBER_Y - 1) {
			selectionY += 1;
		}
	}
	
	public int getSelectionX() {
		return selectionX;
	}
	public void setSelectionX(int selectionX) {
		this.selectionX = selectionX;
	}
	public int getSelectionY() {
		return selectionY;
	}
	public void setSelectionY(int selectionY) {
		this.selectionY = selectionY;
	}
	
}
