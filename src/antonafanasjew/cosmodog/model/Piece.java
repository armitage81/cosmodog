package antonafanasjew.cosmodog.model;

/**
 * Represents a piece on the map. It could be an item, an actor or the player.
 */
public class Piece extends CosmodogModel {

	private static final long serialVersionUID = -7878012563623429751L;

	private int positionX;
	private int positionY;

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

}
