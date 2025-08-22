package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.model.CosmodogGame;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

public class TileBasedMapFactory {

	TileBasedMap createTileBasedMap(Actor actor, CosmodogGame game, CosmodogMap map, CollisionValidator collisionValidator) {
        return new TileBasedMapAdapter(actor, game, map, collisionValidator);
	}
	
}
