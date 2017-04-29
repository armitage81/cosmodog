package antonafanasjew.cosmodog.ingamemenu.inventory;

import antonafanasjew.cosmodog.ingamemenu.InGameMenuInputState;

public class InventoryInputState implements InGameMenuInputState {

	private int selectionX = 0;
	private int selectionY = 0;
	
	public void left() {
		selectionX -= 1;
		if (selectionX < 0) {
			selectionX = InventoryRenderer.INVENTORY_COLUMNS - 1;
		}
	}
	
	public void up() {
		selectionY -= 1;
		if (selectionY < 0) {
			selectionY = InventoryRenderer.INVENTORY_ROWS - 1;
		}
	}
	
	public void right() {
		selectionX = (selectionX + 1) % InventoryRenderer.INVENTORY_COLUMNS;
	}
	
	public void down() {
		selectionY = (selectionY + 1) % InventoryRenderer.INVENTORY_ROWS;
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

	@Override
	public void initializeState() {
		selectionX = 0;
		selectionY = 0;
	}
	
	
	
}
