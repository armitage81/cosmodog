package antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates;

import antonafanasjew.cosmodog.globals.Layers;

public class BottomLayersRenderingPredicate implements MapLayerRendererPredicate {

	@Override
	public boolean tileShouldBeRendered(int layerIndex, int tileX, int tileY) {
		return layerIndex >= 0 && layerIndex < Layers.LAYER_FLOWERS_TIPS;
	}

}
