package antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CollisionUtils;

public class TopLayersRenderingPredicate implements MapLayerRendererPredicate {

	@Override
	public boolean tileShouldBeRendered(int layerIndex, int tileX, int tileY) {
		
		//We can ignore all layers that are not top at this point of time.
		boolean topLayer = layerIndex > Layers.LAYER_RUINS_TIPS && layerIndex < Layers.LAYER_META_COLLISIONS;

		if (!topLayer) {
			return false;
		}
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		//No need to calculate the region intersection if the tile is empty anyway.
		if (map.getTileId(tileX, tileY, layerIndex) == 0) {
			return false;
		}
		
		//If player is not in one of the roof regions then render any top tile
		TiledObject roofRegionOverPlayer = roofRegion(player, map);
		
		if (roofRegionOverPlayer == null) {
			return true;
		}
		
		boolean retVal = !tileCoversPlayer(roofRegionOverPlayer, map, tileX, tileY);
		
		return retVal;
		
		
	}
	
	private boolean tileCoversPlayer(TiledObject regionOverPlayer, CosmodogMap map, int tilePosX, int tilePosY) {
		
		int x = tilePosX * map.getTileWidth();
		int y = tilePosY * map.getTileHeight();
		int w = map.getTileWidth();
		int h = map.getTileHeight();
		
		PlacedRectangle r = PlacedRectangle.fromAnchorAndSize(x, y, w, h);
		
		boolean intersects = CollisionUtils.intersects(r, regionOverPlayer);
		
		return intersects;
	}
	
	private TiledObject roofRegion(Player player, CosmodogMap map) {
		return PlayerMovementCache.getInstance().getRoofRegionOverPlayer();
	}
	

}
