package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Represents a dynamic tile that is an poison spot.
 */
public class Poison extends DynamicPiece {

	private static final long serialVersionUID = 5708806596326635656L;
	
	
	public static final short STATE_DEACTIVATED = 0;
	public static final short STATE_ACTIVATED = 1;
	
	
	private static final short NUMBER_OF_SHAPES = 3;
	
	private static short shapeLoopCounter = 0;
	
	private short state = STATE_ACTIVATED;
	
	private short shapeNumber = (short)((shapeLoopCounter++) % NUMBER_OF_SHAPES);
	
	public short getState() {
		return state;
	}
	
	public void setState(short state) {
		this.state = state;
	}
	
	public boolean isActivated() {
		return state == STATE_ACTIVATED;
	}
	
	public boolean isDeactivated() {
		return state == STATE_DEACTIVATED;
	}
	
	public static Poison create(Position position) {
		Poison poison = new Poison();
		poison.setPosition(position);
		return poison;
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
