package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Defines collision for flying vehicles, which is everything what is not accessible by wheels, foot or boat.  
 */
public class ActorOnWingsCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		boolean collisionTile = TileType.COLLISION.getTileId() == tileId;
		boolean passable = !collisionTile;

		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED;
		return CollisionStatus.instance(actor, map, tileX, tileY, passable, passageBlocker);
		
	}

}
