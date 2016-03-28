package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Contains the common logic for water place validators.
 */
public abstract class AbstractWaterValidator implements WaterValidator {

	@Override
	public boolean waterInReach(Actor actor, CustomTiledMap map, int tileX, int tileY) {
		return waterInReachInternal(actor, map, tileX, tileY);
	}

	protected abstract boolean waterInReachInternal(Actor actor, CustomTiledMap map, int tileX, int tileY);

}
