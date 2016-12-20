package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.model.DynamicPiece;

/**
 * Represents a dynamic tile that is a crate.
 */
public class Crate extends DynamicPiece {

	private static final long serialVersionUID = 5708806596326635656L;

	public static final short STATE_WHOLE = 0;
	public static final short STATE_DAMAGED = 1;
	public static final short STATE_BADLY_DAMAGED = 2;
	public static final short STATE_DESTROYED = 3;

	private static final short NUMBER_OF_SHAPES = 5;
	private static short shapeLoopCounter = 0;

	private short state = STATE_WHOLE;
	private short shapeNumber = (short) ((shapeLoopCounter++) % NUMBER_OF_SHAPES);

	public short getState() {
		return state;
	}

	public boolean isDestroyed() {
		return state == STATE_DESTROYED;
	}

	public static Crate create(int x, int y) {
		Crate stone = new Crate();
		stone.setPositionX(x);
		stone.setPositionY(y);
		return stone;
	}

	public String animationSuffixFromState() {
		return String.valueOf(state);
	}

	public short getShapeNumber() {
		return shapeNumber;
	}

	@Override
	public void interact() {
		if (state < STATE_DESTROYED) {
			state++;
		}
	}

	@Override
	public boolean wrapsCollectible() {
		return state != STATE_DESTROYED;
	}

	
}
