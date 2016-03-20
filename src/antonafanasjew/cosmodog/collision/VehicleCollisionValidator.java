package antonafanasjew.cosmodog.collision;

import org.newdawn.slick.tiled.TiledMap;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.Tiles;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Vehicle;

/**
 * Collision validator for vehicles.
 */
public class VehicleCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, TiledMap map, int tileX, int tileY) {
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		boolean collisionTile = tileId == Tiles.COLLISION_TILE_ID;
		boolean collisionVehicleTile = tileId == Tiles.COLLISION_VEHICLE_TILE_ID;
		boolean waterTile = tileId == Tiles.WATER_TILE_ID;
		
		boolean passable = !collisionTile && !collisionVehicleTile && ! waterTile;
		
		Piece piece = cosmodogMap.pieceAtTile(tileX, tileY);
		boolean vehicleOnTile = piece instanceof Vehicle;
		boolean resultingPassageFlag = passable && !vehicleOnTile;
		int noPassageReason = resultingPassageFlag ? CollisionStatus.NO_PASSAGE_REASON_NO_REASON : CollisionStatus.NO_PASSAGE_REASON_BLOCKED;
		return CollisionStatus.instance(actor, map, tileX, tileY, resultingPassageFlag, noPassageReason);
	}

}
