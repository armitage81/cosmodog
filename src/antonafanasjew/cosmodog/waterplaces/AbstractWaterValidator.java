package antonafanasjew.cosmodog.waterplaces;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Contains the common logic for water place validators.
 */
public abstract class AbstractWaterValidator implements WaterValidator {

	@Override
	public boolean waterInReach(Actor actor, CosmodogMap map, int tileX, int tileY) {
		return waterInReachInternal(actor, map, tileX, tileY);
	}

	protected abstract boolean waterInReachInternal(Actor actor, CosmodogMap map, int tileX, int tileY);

}
