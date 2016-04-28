package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Default validator for water places. It checks if the tile on the actors position
 * is marked as waterplace or near waterplace.
 *
 */
public class DefaultWaterValidator extends AbstractWaterValidator {

	@Override
	protected boolean waterInReachInternal(Actor actor, CustomTiledMap map, int tileX, int tileY) {
		int waterLayerTileId = map.getTileId(actor.getPositionX(), actor.getPositionY(), Layers.LAYER_META_WATERPLACES);
		return  (TileType.WATERPLACE.getTileId() == waterLayerTileId || TileType.NEAR_WATERPLACE.getTileId() == waterLayerTileId);
	}

}
