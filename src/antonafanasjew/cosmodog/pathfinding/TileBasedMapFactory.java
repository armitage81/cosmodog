package antonafanasjew.cosmodog.pathfinding;

import org.newdawn.slick.util.pathfinding.TileBasedMap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.actors.Actor;

public class TileBasedMapFactory {

	TileBasedMap createTileBasedMap(Actor actor, ApplicationContext applicationContext, CustomTiledMap tiledMap, CollisionValidator collisionValidator, TravelTimeCalculator travelTimeCalculator) {
		TileBasedMap retVal = new TileBasedMapAdapter(actor, applicationContext, tiledMap, collisionValidator, travelTimeCalculator);
		return retVal;
	}
	
}
