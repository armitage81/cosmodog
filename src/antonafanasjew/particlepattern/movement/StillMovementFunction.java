package antonafanasjew.particlepattern.movement;

import antonafanasjew.cosmodog.topology.Vector;

public class StillMovementFunction extends AbstractMovementFunction {
	
	public StillMovementFunction(float millisecondInUnit) {
		super(millisecondInUnit);
	}

	@Override
	public Vector applyInternal(float units) {
		return Vector.empty();
	}

}
