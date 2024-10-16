package antonafanasjew.cosmodog.collision.validators.npc;

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
 * Defines collision for swimming vehicles, which is only water.  
 */
public class SailingCollisionValidatorForNpc extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		int tileId = map.getTileId(position, Layers.LAYER_META_COLLISIONS);
        boolean passable = TileType.COLLISION_WATER.getTileId() == tileId;
		PassageBlockerType passageBlocker = PassageBlockerType.PASSABLE;
		return CollisionStatus.instance(actor, map, position, passable, passageBlocker);
	}

}
