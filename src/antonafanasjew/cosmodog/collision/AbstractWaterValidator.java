package antonafanasjew.cosmodog.collision;

import org.newdawn.slick.tiled.TiledMap;

import antonafanasjew.cosmodog.model.actors.Actor;


public abstract class AbstractWaterValidator implements WaterValidator {

	@Override
	public boolean waterInReach(Actor actor, TiledMap map, int tileX, int tileY) {
		return waterInReachInternal(actor, map, tileX, tileY);
	}

	protected abstract boolean waterInReachInternal(Actor actor, TiledMap map, int tileX, int tileY);

}
