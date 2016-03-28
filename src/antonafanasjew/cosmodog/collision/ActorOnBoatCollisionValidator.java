package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.Tiles;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Defines collision for swimming vehicles, which is only water.  
 */
public class ActorOnBoatCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		boolean waterTile = tileId == Tiles.WATER_TILE_ID;
		boolean passable = waterTile;
		int noPassageReason = passable ? CollisionStatus.NO_PASSAGE_REASON_NO_REASON : CollisionStatus.NO_PASSAGE_REASON_BLOCKED;
		return CollisionStatus.instance(actor, map, tileX, tileY, passable, noPassageReason);
	}

}
