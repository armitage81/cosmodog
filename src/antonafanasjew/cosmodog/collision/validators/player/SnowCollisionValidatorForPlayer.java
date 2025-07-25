package antonafanasjew.cosmodog.collision.validators.player;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Collision validator for player in snow. He can move through it only if he has the ski equippment.
 * 
 */
public class SnowCollisionValidatorForPlayer extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		
		int tileId = map.getTileId(entrance.getPosition(), Layers.LAYER_META_COLLISIONS);
		Player player = (Player)actor;
		
		VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE); 
		
		boolean hasSki = player.getInventory().get(InventoryItemType.SKI) != null;
		
		boolean hasVehicle = vehicleInventoryItem != null && !vehicleInventoryItem.isExiting();
		
		boolean snowTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_SNOW);
		
		boolean passable = !snowTile || (hasSki && !hasVehicle);
		
		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED;
		return CollisionStatus.instance(actor, map, entrance, passable, passageBlocker);
	}

}
