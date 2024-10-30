package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.topology.Position;

import java.io.Serial;

/**
 * Represents a dynamic tile that is a crate.
 */
public class Crate extends DynamicPiece {

	@Serial
	private static final long serialVersionUID = 5708806596326635656L;

	public static final short STATE_WHOLE = 0;
	public static final short STATE_DAMAGED = 1;
	public static final short STATE_BADLY_DAMAGED = 2;
	public static final short STATE_DESTROYED = 3;

	private static final short NUMBER_OF_SHAPES = 5;
	private static short shapeLoopCounter = 0;

	private short state = STATE_WHOLE;
	private final short shapeNumber = (short) ((shapeLoopCounter++) % NUMBER_OF_SHAPES);

	public short getState() {
		return state;
	}

	public boolean isDestroyed() {
		return state == STATE_DESTROYED;
	}

	public static Crate create(Position position) {
		Crate stone = new Crate();
		stone.setPosition(position);
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
			
			if (state == STATE_WHOLE) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_BREAK_CRATE1).play();
			} else if (state == STATE_DAMAGED) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_BREAK_CRATE2).play();
			} else if (state == STATE_BADLY_DAMAGED) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_BREAK_CRATE2).play();
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_BREAK_CRATE3).play();
			}
			
			
			state++;
		}
	}

	@Override
	public boolean wrapsCollectible() {
		return state != STATE_DESTROYED;
	}

	@Override
	public String animationId(boolean bottomNotTop) {
		String animationIdPrefix = "dynamicPieceCrate";
		String animationIdPrefixIndex = String.valueOf(getShapeNumber());
		String animationIdInfix = bottomNotTop ? "Bottom" : "Top";
		String animationSuffix = animationSuffixFromState();
        return animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
	}
}
