package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Represents a dynamic tile that is a binary indicator (e.g. trafic light with green and red statuses).
 */
public class BinaryIndicator extends DynamicPiece {

	private static final long serialVersionUID = 5708806596326635656L;

	public static final short STATE_FALSE = 0;
	public static final short STATE_TRUE = 1;
	
	
	private short state = STATE_FALSE;
	
	public short getState() {
		return state;
	}
	
	public boolean isTrue() {
		return state == STATE_TRUE;
	}
	
	public static BinaryIndicator create(Position position) {
		BinaryIndicator binaryIndicator = new BinaryIndicator();
		binaryIndicator.setPosition(position);
		return binaryIndicator;
	}
	
	public String animationSuffixFromState() {
		return String.valueOf(state);
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
		return false;
	}

	public void setState(short state) {
		this.state = state;
	}

	@Override
	public String animationId(boolean bottomNotTop) {
		String animationIdPrefix = "dynamicPieceAlienSwitch";
		String animationIdInfix = bottomNotTop ? "Bottom" : "Top";
		String animationSuffix = animationSuffixFromState();
        return animationIdPrefix + animationIdInfix + animationSuffix;
	}
}
