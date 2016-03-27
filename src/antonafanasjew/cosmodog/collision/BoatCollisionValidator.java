package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.Tiles;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Collision validator for the player with a boat.
 *
 */
public class BoatCollisionValidator extends AbstractCollisionValidator {

	/**
	 * Checks the collision layer on the tiled map and returns passable status in case the tile is not blocked (It does not check water tiles as the player has the boat).
	 */
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		boolean collisionTile = tileId == Tiles.COLLISION_TILE_ID;
		boolean passable = !collisionTile; //Water tile is allowedd
		return CollisionStatus.instance(actor, map, tileX, tileY, passable, CollisionStatus.NO_PASSAGE_REASON_BLOCKED);
	}

}
