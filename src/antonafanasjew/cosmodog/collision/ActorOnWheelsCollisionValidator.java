package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.Tiles;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Defines collision for wheeled NPC's.  
 */
public class ActorOnWheelsCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		boolean collisionTile = tileId == Tiles.COLLISION_TILE_ID;
		boolean collisionVehicleTile = tileId == Tiles.COLLISION_VEHICLE_TILE_ID;
		boolean waterTile = tileId == Tiles.WATER_TILE_ID;
		
		boolean passable = !collisionTile && !collisionVehicleTile && ! waterTile;
		
		int noPassageReason = passable ? CollisionStatus.NO_PASSAGE_REASON_NO_REASON : CollisionStatus.NO_PASSAGE_REASON_BLOCKED;
		return CollisionStatus.instance(actor, map, tileX, tileY, passable, noPassageReason);
		
	}

}
