package antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates;

import java.util.Collection;
import java.util.Set;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.structures.TileCoordinates;
import antonafanasjew.cosmodog.util.TileUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class TopLayersRenderingPredicate implements MapLayerRendererPredicate {

	private Set<TileCoordinates> playerCoveringStructure = Sets.newHashSet();
	
	public TopLayersRenderingPredicate() {
		ApplicationContext applicationContext = ApplicationContext.instance();
		//We need the player position to check if he is covered by a top tile.
		CosmodogGame cosmodogGame = applicationContext.getCosmodog().getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
		int px = player.getPositionX();
		int py = player.getPositionY();
		
		//We iterate only through covering top layers (and ignore unimportant layers, like electricity cables, towers etc.)
		for (int i : Layers.COVERING_TOP_LAYERS) {
			//We check the tile at player position on the covering layer.
			int topTileId = applicationContext.getTiledMap().getTileId(px, py, i);
			if (topTileId > 0) {
				
				//We find all neighbour ids to mark the region that should be skipped. We start with the player position (not the actual tile we are iterating through)
				playerCoveringStructure = TileUtils.getConnectedElements(px, py, i, applicationContext.getTiledMap(), new Predicate<TileCoordinates>() {
					
					@Override
					public boolean apply(TileCoordinates tc) {
						int tileId = applicationContext.getTiledMap().getTileId(tc.getX(), tc.getY(), i);
						return tileId > 0;
					}
				});			
			}
			
		}
	}
	
	@Override
	public boolean tileShouldBeRendered(int layerIndex, int tileX, int tileY) {
		
		//We can ignore all layers that are not top at this point of time.
		boolean topLayer = layerIndex > Layers.LAYER_RUINS_TIPS && layerIndex < Layers.LAYER_META_COLLISIONS;
		if (! topLayer) {
			return false;
		}
		
		//We want to find all top tiles to be skipped while rendering if they are covering the player (f.i. roofs)
		boolean shouldBeSkipped = false;
		//We iterate only through covering top layers (and ignore unimportant layers, like electricity cables, towers etc.)
		for (int i : Layers.COVERING_TOP_LAYERS) {
			
			//We skip the tile rendering if it is on the referenced layer and neighbour to the tile covering the player.
			Collection<Integer> removableLayers = Layers.COVERING_2_REMOVABLE_LAYERS.get(i);
			if (removableLayers.contains(layerIndex)) {
				if (playerCoveringStructure.contains(new TileCoordinates(tileX, tileY, i))) {
					shouldBeSkipped = true;
				}
			}
			
		}
			
		
		return !shouldBeSkipped;
	}

}
