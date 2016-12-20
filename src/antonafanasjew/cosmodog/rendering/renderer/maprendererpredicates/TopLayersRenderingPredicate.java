package antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates;

import java.util.List;
import java.util.Map;
import java.util.Set;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.tiledmap.TiledMapLayer;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CollisionUtils;

import com.google.common.collect.Lists;

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
		Set<TiledObject> roofRegionsOverPlayer = roofRegions(player, map);
		
		if (roofRegionsOverPlayer.isEmpty()) {
			return true;
		}
		
		boolean retVal = !tileCoversPlayer(roofRegionsOverPlayer, map, tileX, tileY, layerIndex);
		
		return retVal;
		
		
	}
	
	private boolean tileCoversPlayer(Set<TiledObject> regionsOverPlayer, CosmodogMap map, int tilePosX, int tilePosY, int layerIndex) {
		
		int x = tilePosX * map.getTileWidth();
		int y = tilePosY * map.getTileHeight();
		int w = map.getTileWidth();
		int h = map.getTileHeight();
		
		TiledMapLayer layer = map.getMapLayers().get(layerIndex);
		String layerName = layer.getName();
		
		PlacedRectangle r = PlacedRectangle.fromAnchorAndSize(x, y, w, h);
		
		boolean intersects = false;
		
		for (TiledObject regionOverPlayer : regionsOverPlayer) {

			//Some roof regions specify layers which represent the roofs to avoid clipping all top layers.
			//In such cases, the region will have a property in form layers=<layerA>,<layerB>. All other layers
			//need to be rendered.
			Map<String, String> properties = regionOverPlayer.getProperties();
			if (properties != null) {
				String relevantLayersProperty = properties.get("layers");
				if (relevantLayersProperty != null) {
					List<String> relevantLayers = Lists.newArrayList(relevantLayersProperty.split(","));
					if (!relevantLayers.contains(layerName)) {
						continue;
					}
					
				}
			}
			
			if (CollisionUtils.intersects(r, regionOverPlayer)) {
				intersects = true;
				break;
			}
		}
		
		return intersects;
	}
	
	private Set<TiledObject> roofRegions(Player player, CosmodogMap map) {
		return PlayerMovementCache.getInstance().getRoofRegionsOverPlayer();
	}
	

}
