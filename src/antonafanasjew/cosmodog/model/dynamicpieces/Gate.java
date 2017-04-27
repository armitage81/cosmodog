package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.DynamicPiece;

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
	
	public static Gate create(int x, int y) {
		Gate gate = new Gate();
		gate.setPositionX(x);
		gate.setPositionY(y);
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

}
