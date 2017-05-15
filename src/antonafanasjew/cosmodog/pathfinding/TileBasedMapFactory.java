package antonafanasjew.cosmodog.pathfinding;

import org.newdawn.slick.util.pathfinding.TileBasedMap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

public class TileBasedMapFactory {

	TileBasedMap createTileBasedMap(Actor actor, ApplicationContext applicationContext, CosmodogMap map, CollisionValidator collisionValidator) {
		TileBasedMap retVal = new TileBasedMapAdapter(actor, applicationContext, map, collisionValidator);
		return retVal;
	}
	
}
