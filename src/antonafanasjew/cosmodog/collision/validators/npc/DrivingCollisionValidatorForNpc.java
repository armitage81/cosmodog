package antonafanasjew.cosmodog.collision.validators.npc;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Defines collision for wheeled NPC's.  
 */
public class DrivingCollisionValidatorForNpc extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		int tileId = map.getTileId(entrance.getPosition(), Layers.LAYER_META_COLLISIONS);
		boolean collisionTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION);
		boolean collisionVehicleTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_VEHICLE);
		boolean waterTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_WATER);
		boolean snowTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_SNOW);
		
		boolean passable = !collisionTile && !collisionVehicleTile && !waterTile && !snowTile;
		
		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED_ON_WHEELS;
		return CollisionStatus.instance(actor, map, entrance, passable, passageBlocker);
		
	}

}
