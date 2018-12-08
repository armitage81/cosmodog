package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class FeatureBoundRenderer implements Renderer {

	private Renderer delegate;
	private String feature;

	public FeatureBoundRenderer(Renderer delegate, String feature) {
		super();
		this.delegate = delegate;
		this.feature = feature;
	}

	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext) {
		render(gameContainer, graphics, null);
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		Features.getInstance().featureBoundProcedure(feature, new Runnable() {
			@Override
			public void run() {
				delegate.render(gameContainer, graphics, renderingParameter);
			}
		});		
	}

}
