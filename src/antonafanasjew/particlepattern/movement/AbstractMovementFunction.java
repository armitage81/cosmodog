package antonafanasjew.particlepattern.movement;

import antonafanasjew.cosmodog.topology.Vector;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

public abstract class AbstractMovementFunction implements Function<Long, Vector> {

	private float millisecondsInUnit;
	
	public AbstractMovementFunction(float millisecondInUnit) {
		Preconditions.checkArgument(millisecondInUnit > 0);
		this.millisecondsInUnit = millisecondInUnit;
	}
	
	@Override
	public Vector apply(Long t) {
		Preconditions.checkArgument(t >= 0);
		return applyInternal(t / millisecondsInUnit);
	}

	protected abstract Vector applyInternal(float units);

	public float getMillisecondsInUnit() {
		return millisecondsInUnit;
	}

}
