package antonafanasjew.cosmodog.particlepattern.movement;

import antonafanasjew.cosmodog.topology.Vector;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

/**
 * Represents a function that calculates a movement vector for a given time.
 * <p>
 * The function is defined by a time unit in milliseconds. The function is then applied to a time in milliseconds
 * and returns a movement vector.
 * <p>
 * This function can be used to move particles in a pattern over time. For instance, a function could be defined
 * that moves particles in a circle. The function would then be applied to a time in milliseconds and return the
 * position of the particle at that time.
 * <p>
 * Another example: Birds flying over the game field. The function could be defined to move the birds in a linear way.
 * <p>
 * Yet another example: Falling snowflake particles could be moved in a sinusoidal pattern combined with a linear movement down.
 * to represent the falling of the snowflakes.
 * <p>
 * This class is abstract, that is the actual movement calculation must be implemented in subclasses.
 * Take note: The subclasses do not calculate with milliseconds but with time units.
 * The conversion is done in this class by providing in the constructor the information how many milliseconds should
 * be in a time unit. The bigger the value, the slower the movement. The value 1 means that the time unit is in milliseconds
 * and the movement is calculated in milliseconds. The value 10 means that the time unit is in 10 milliseconds and the movement
 * is 10 times slower.
 */
public abstract class AbstractMovementFunction implements Function<Long, Vector> {

	private final float millisecondsInUnit;
	
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
