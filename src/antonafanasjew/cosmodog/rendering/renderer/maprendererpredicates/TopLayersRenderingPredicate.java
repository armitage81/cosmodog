package antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates;

import java.util.Map;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CollisionUtils;
import antonafanasjew.cosmodog.util.ObjectGroupUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TopLayersRenderingPredicate implements MapLayerRendererPredicate {

	/*
	 * There is a method checking if there is a roof over the player position
	 * in each rendering cycle. It is not necessary as long as the player does not move.
	 * So we add a cache that holds the needed information for each visited player position.
	 * It can maximally hold 16.000 elements. 
	 */
	//private Map<Position, Boolean> roofRegionOverPlayer = Maps.newHashMap();
	
	
	private LoadingCache<Position, Boolean> roofRegionOverPlayer = CacheBuilder.newBuilder().build(new CacheLoader<Position, Boolean>() {

		@Override
		public Boolean load(Position arg0) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
		
	});
	
	@Override
	public boolean tileShouldBeRendered(int layerIndex, int tileX, int tileY) {
		
		//We can ignore all layers that are not top at this point of time.
		boolean topLayer = layerIndex > Layers.LAYER_RUINS_TIPS && layerIndex < Layers.LAYER_META_COLLISIONS;
		if (!topLayer) {
			return false;
		}

		
		Player player = ApplicationContextUtils.getPlayer();
		CustomTiledMap map = ApplicationContextUtils.getCustomTiledMap();
		
		//If player is not in one of the roof regions then render any top tile
		TiledObject roofRegionOverPlayer = roofRegion(player, map);
		
		if (roofRegionOverPlayer == null) {
			return true;
		}
		
		return !tileCoversPlayer(roofRegionOverPlayer, map, tileX, tileY);
		
	}
	
	private boolean tileCoversPlayer(TiledObject regionOverPlayer, CustomTiledMap map, int tilePosX, int tilePosY) {
		
		int x = tilePosX * map.getTileWidth();
		int y = tilePosY * map.getTileHeight();
		int w = map.getTileWidth();
		int h = map.getTileHeight();
		
		PlacedRectangle r = PlacedRectangle.fromAnchorAndSize(x, y, w, h);
		
		boolean intersects = CollisionUtils.intersects(r, regionOverPlayer);
		
		return intersects;
	}
	
	private TiledObject roofRegion(Player player, CustomTiledMap map) {
		
		Map<String, TiledObject> roofRegions = map.getObjectGroups().get(ObjectGroupUtils.OBJECT_GROUP_ID_ROOFS).getObjects();
		
		for (TiledObject roofRegion : roofRegions.values()) {
		
			if (RegionUtils.playerInRegion(player, roofRegion, map.getTileWidth(), map.getTileHeight())) {
				return roofRegion;
			}
			
			
		}
		
		return null;
	}
	

}
