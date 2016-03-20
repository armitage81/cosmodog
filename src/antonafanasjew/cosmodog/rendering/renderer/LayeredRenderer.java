package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Implementation of layers for rendering.
 * Organizing renderers in layers lets the programmer add
 * additional rendering objects in any order while keeping the rendering order
 */
public class LayeredRenderer implements Renderer {

	private Multimap<Integer, Renderer> underlyingRenderers = ArrayListMultimap.create();
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		Set<Integer> keys = underlyingRenderers.keySet();
		List<Integer> sortedKeys = Lists.newArrayList(keys);
		Collections.sort(sortedKeys);
		for (Integer key : sortedKeys) {
			Collection<Renderer> renderersForLayer = underlyingRenderers.get(key);
			for (Renderer renderer : renderersForLayer) {
				renderer.render(gameContainer, graphics, drawingContext, renderingParameter);
			}
		}
	}
	
	public void addRendererToLayer(int layer, Renderer renderer) {
		underlyingRenderers.put(layer, renderer);
	}

}
