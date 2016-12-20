package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.model.DynamicPiece;

/**
 * Represents a dynamic tile that is an infantry mine that can be deactivated by the player.
 */
public class Mine extends DynamicPiece {

	private static final long serialVersionUID = 5708806596326635656L;
	
	
	public static final int DAMAGE_TO_PLAYER = 5;
	
	public static final short STATE_INVISIBLE = 0;
	public static final short STATE_VISIBLE = 1;
	public static final short STATE_DESTROYED = 2;
	public static final short STATE_DEACTIVATED = 3;
	
	
	private static final short NUMBER_OF_SHAPES = 4;
	
	private static short shapeLoopCounter = 0;
	
	private short state = STATE_INVISIBLE;
	private short shapeNumber = (short)((shapeLoopCounter++) % NUMBER_OF_SHAPES);
	
	public short getState() {
		return state;
	}
	
	public void setState(short state) {
		this.state = state;
	}
	
	public boolean isDestroyed() {
		return state == STATE_DESTROYED;
	}
	
	public static Mine create(int x, int y) {
		Mine mine = new Mine();
		mine.setPositionX(x);
		mine.setPositionY(y);
		return mine;
	}
	
	public String animationSuffixFromState() {
		return String.valueOf(state);
	}

	public short getShapeNumber() {
		return shapeNumber;
	}

	@Override
	public void interact() {

	}

	@Override
	public boolean wrapsCollectible() {
		return false;
	}
	
}
