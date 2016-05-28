package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Defines collision for swimming vehicles, which is only water.  
 */
public class ActorOnBoatCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		boolean waterTile = TileType.COLLISION_WATER.getTileId() == tileId;
		boolean passable = waterTile;
		PassageBlocker passageBlocker = passable ? PassageBlocker.PASSABLE : PassageBlocker.PASSABLE;
		return CollisionStatus.instance(actor, map, tileX, tileY, passable, passageBlocker);
	}

}
