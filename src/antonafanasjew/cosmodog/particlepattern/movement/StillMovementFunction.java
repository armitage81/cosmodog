package antonafanasjew.cosmodog.particlepattern.movement;

import antonafanasjew.cosmodog.topology.Vector;

/**
 * Represents a movement function that does not move at all.
 * Regardless of the given time, the offset vector will be empty.
 */
public class StillMovementFunction extends AbstractMovementFunction {
	
	public StillMovementFunction(float millisecondInUnit) {
		super(millisecondInUnit);
	}

	@Override
	public Vector applyInternal(float units) {
		return Vector.empty();
	}

}
