package antonafanasjew.cosmodog.collision.validators.moveable;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

/**
 * This validator blocks every tile that is not green (in terms of collision meta layer).
 */
public class GreenOnlyCollisionValidatorForMoveable extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		int tileId = map.getTileId(position, Layers.LAYER_META_COLLISIONS);
		boolean noCollision = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_FREE);
		PassageBlockerType passageBlocker = noCollision ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED;
		return CollisionStatus.instance(actor, map, position, noCollision, passageBlocker);
		
	}

}
