package antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.topology.Position;

public class BottomLayersRenderingPredicate implements MapLayerRendererPredicate {

	@Override
	public boolean tileShouldBeRendered(int layerIndex, Position position) {
		return layerIndex >= 0 && layerIndex < Layers.LAYER_FLOWERS_TIPS;
	}

}
