package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.model.DynamicPiece;

/**
 * Represents a dynamic tile that is apressure button.
 */
public class PressureButton extends DynamicPiece {

	private static final long serialVersionUID = 5708806596326635656L;
	
	
	public static final short STATE_DEACTIVATED = 0;
	public static final short STATE_ACTIVATED = 1;
	
	
	private static final short NUMBER_OF_SHAPES = 1;
	
	private static short shapeLoopCounter = 0;
	
	private short state = STATE_DEACTIVATED;
	
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
	
	public static PressureButton create(int x, int y) {
		PressureButton button = new PressureButton();
		button.setPositionX(x);
		button.setPositionY(y);
		return button;
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
