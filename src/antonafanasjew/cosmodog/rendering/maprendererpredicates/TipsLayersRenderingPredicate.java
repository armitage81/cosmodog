package antonafanasjew.cosmodog.rendering.maprendererpredicates;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.topology.Position;

public class TipsLayersRenderingPredicate implements MapLayerRendererPredicate {

	@Override
	public boolean tileShouldBeRendered(int layerIndex, Position position) {
		return layerIndex >= Layers.LAYER_FLOWERS_TIPS && layerIndex <= Layers.LAYER_RUINS_TIPS;
	}

}
