package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Represents a dynamic tile that is a secret door that can be opened.
 */
public class SecretDoor extends DynamicPiece {

	private static final long serialVersionUID = 5708806596326635656L;

	public static final String STIL_SPIKES = "spikes";
	public static final String STIL_HYDRAULICS = "hydraulics";
	public static final String STIL_WALL = "wall";
	public static final String STIL_ENERGY = "energy";
	
	public static final short STATE_CLOSED = 0;
	public static final short STATE_OPENING_PHASE1 = 1;
	public static final short STATE_OPENING_PHASE2 = 2;
	public static final short STATE_OPENING_PHASE3 = 3;
	public static final short STATE_OPEN = 4;
	
	
	private short state = STATE_CLOSED;
	
	private String stil;
	
	public short getState() {
		return state;
	}
	
	public void setState(short state) {
		this.state = state;
	}
	
	public String getStil() {
		return stil;
	}
	
	public void setStil(String stil) {
		this.stil = stil;
	}
	
	public boolean isOpen() {
		return state == STATE_OPEN;
	}
	
	public static SecretDoor create(Position position) {
		SecretDoor door = new SecretDoor();
		door.setPosition(position);
		return door;
	}
	
	public String animationSuffixFromState() {
		return String.valueOf(state);
	}

	@Override
	public void interact() {
		if (state != STATE_OPEN) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_NOWAY).play();
		}
	}

	@Override
	public boolean wrapsCollectible() {
		return false;
	}

	@Override
	public String animationId(boolean bottomNotTop) {
		String animationIdPrefix = "dynamicPieceSecretDoor";
		String animationIdStil = getStil().substring(0, 1).toUpperCase() + getStil().substring(1);
		String animationIdInfix = bottomNotTop ? "Bottom" : "Top";
		String animationSuffix = animationSuffixFromState();
        return animationIdPrefix + animationIdStil + animationIdInfix + animationSuffix;
	}
}
