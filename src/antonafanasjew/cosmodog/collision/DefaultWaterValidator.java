package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.Tiles;
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
		return  (waterLayerTileId == Tiles.WATERPLACE_TILE_ID || waterLayerTileId == Tiles.NEAR_WATERPLACE_TILE_ID);
	}

}
