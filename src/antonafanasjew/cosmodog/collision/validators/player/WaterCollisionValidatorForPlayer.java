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
import antonafanasjew.cosmodog.topology.Position;

/**
 * Collision validator for player in water. He can enter it only if he has a boat.
 */
public class WaterCollisionValidatorForPlayer extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		int tileId = map.getTileId(position, Layers.LAYER_META_COLLISIONS);
		Player player = (Player)actor;
		
		VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE); 
		
		boolean hasBoat = player.getInventory().get(InventoryItemType.BOAT) != null;
		
		boolean hasVehicle = vehicleInventoryItem != null && !vehicleInventoryItem.isExiting();
		
		boolean waterTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_WATER);
		
		boolean passable = !waterTile || (hasBoat && !hasVehicle);
		
		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED;
		return CollisionStatus.instance(actor, map, position, passable, passageBlocker);
	}

}
