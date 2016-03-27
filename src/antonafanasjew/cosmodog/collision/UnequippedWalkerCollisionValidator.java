package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.Tiles;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Collision validator for actors that are not equipped and are walking on foot (no vehicles)
 * Usually, such walkers can cross normal and rough terrain (latter is inaccessible for cars), but cannot
 * cross water.
 */
public class UnequippedWalkerCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		boolean collisionTile = tileId == Tiles.COLLISION_TILE_ID;
		boolean waterTile = tileId == Tiles.WATER_TILE_ID;
		boolean passable = !collisionTile && ! waterTile;
		int noPassageReason = passable ? CollisionStatus.NO_PASSAGE_REASON_NO_REASON : CollisionStatus.NO_PASSAGE_REASON_BLOCKED;
		return CollisionStatus.instance(actor, map, tileX, tileY, passable, noPassageReason);
	}

}
