package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;

/**
 * Collision validator for actors that are not equipped and are walking on foot (no vehicles)
 * Usually, such walkers can cross normal and rough terrain (latter is inaccessible for cars), but cannot
 * cross water.
 */
public class SnowCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		Player player = (Player)actor;
		
		VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE); 
		
		boolean hasSki = player.getInventory().get(InventoryItemType.SKI) != null;
		
		boolean hasVehicle = vehicleInventoryItem != null && !vehicleInventoryItem.isExiting();
		
		boolean snowTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_SNOW);
		
		boolean passable = !snowTile || (hasSki && !hasVehicle);
		
		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED;
		return CollisionStatus.instance(actor, map, tileX, tileY, passable, passageBlocker);
	}

}
