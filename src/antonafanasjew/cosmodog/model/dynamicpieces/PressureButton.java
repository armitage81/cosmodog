package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Represents a dynamic tile that is a pressure button.
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
	
	public static PressureButton create(Position position) {
		PressureButton button = new PressureButton();
		button.setPosition(position);
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

	@Override
	public boolean permeableForPortalRay(DirectionType incomingDirection) {
		return true;
	}

	@Override
	public String animationId(boolean bottomNotTop) {
		String animationIdPrefix = "dynamicPiecePressureButton";
		String animationIdPrefixIndex = String.valueOf(getShapeNumber());
		String animationIdInfix = bottomNotTop ? "Bottom" : "Top";
		String animationSuffix = animationSuffixFromState();
        return animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
	}
}
