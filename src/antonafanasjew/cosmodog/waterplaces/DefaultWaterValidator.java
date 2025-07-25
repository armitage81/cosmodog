package antonafanasjew.cosmodog.waterplaces;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

/**
 * Default validator for water places. It checks if the tile on the actors position
 * is marked as waterplace or near waterplace.
 *
 * An exception is if the player is on the platform. In this case, water is unreachable
 */
public class DefaultWaterValidator extends AbstractWaterValidator {

	@Override
	protected boolean waterInReachInternal(Actor actor, CosmodogMap map, Position position) {
		
		boolean isOnPlatform = CosmodogMapUtils.isTileOnPlatform(actor.getPosition());
		boolean isInPlatform = actor instanceof Player && ((Player)actor).getInventory().get(InventoryItemType.PLATFORM) != null;
		
		if (isOnPlatform) {
			return false;
		}
		
		if (isInPlatform) {
			return false;
		}
		
		int waterLayerTileId = map.getTileId(actor.getPosition(), Layers.LAYER_META_WATERPLACES);
		return  (TileType.WATERPLACE.getTileId() == waterLayerTileId || TileType.NEAR_WATERPLACE.getTileId() == waterLayerTileId);
	}

}
