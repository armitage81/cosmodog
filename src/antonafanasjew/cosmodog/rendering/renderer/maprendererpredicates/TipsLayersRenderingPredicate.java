package antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates;

import antonafanasjew.cosmodog.globals.Layers;

public class TipsLayersRenderingPredicate implements MapLayerRendererPredicate {

	@Override
	public boolean tileShouldBeRendered(int layerIndex, int tileX, int tileY) {
		return layerIndex >= Layers.LAYER_FLOWERS_TIPS && layerIndex <= Layers.LAYER_RUINS_TIPS + 1;
	}

}
