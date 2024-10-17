package antonafanasjew.cosmodog.waterplaces;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Contains the common logic for water place validators.
 */
public abstract class AbstractWaterValidator implements WaterValidator {

	@Override
	public boolean waterInReach(Actor actor, CosmodogMap map, Position position) {
		return waterInReachInternal(actor, map, position);
	}

	protected abstract boolean waterInReachInternal(Actor actor, CosmodogMap map, Position position);

}
