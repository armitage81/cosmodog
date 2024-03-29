package antonafanasjew.cosmodog.collision.validators.npc;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Defines collision for swimming vehicles, which is only water.  
 */
public class SailingCollisionValidatorForNpc extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		boolean waterTile = TileType.COLLISION_WATER.getTileId() == tileId;
		boolean passable = waterTile;
		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.PASSABLE;
		return CollisionStatus.instance(actor, map, tileX, tileY, passable, passageBlocker);
	}

}
