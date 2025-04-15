package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Represents a dynamic tile that is a gate that can be opened.
 */
public class Gate extends DynamicPiece {

	private static final long serialVersionUID = 5708806596326635656L;

	public static final short STATE_RAISED = 0;
	public static final short STATE_LOWERING_PHASE1 = 1;
	public static final short STATE_LOWERING_PHASE2 = 2;
	public static final short STATE_LOWERING_PHASE3 = 3;
	public static final short STATE_LOWERING_PHASE4 = 4;
	public static final short STATE_LOWERED = 5;
	
	
	private short state = STATE_RAISED;
	
	public short getState() {
		return state;
	}
	
	public void setState(short state) {
		this.state = state;
	}
	
	public boolean isLowered() {
		return state == STATE_LOWERED;
	}
	
	public static Gate create(Position position) {
		Gate gate = new Gate();
		gate.setPosition(position);
		return gate;
	}
	
	public String animationSuffixFromState() {
		return String.valueOf(state);
	}

	@Override
	public void interact() {
		if (state != STATE_LOWERED) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_NOWAY).play();
		}
	}

	@Override
	public boolean wrapsCollectible() {
		return false;
	}

	@Override
	public boolean permeableForPortalRay(DirectionType incomingDirection) {
		return false;
	}

	@Override
	public String animationId(boolean bottomNotTop) {
		String animationIdPrefix = "dynamicPieceGate";
		String animationIdInfix = bottomNotTop ? "Bottom" : "Top";
		String animationSuffix = animationSuffixFromState();
        return animationIdPrefix + animationIdInfix + animationSuffix;
	}
}
