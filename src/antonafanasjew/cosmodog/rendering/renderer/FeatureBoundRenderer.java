package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class FeatureBoundRenderer extends AbstractRenderer {

	private final Renderer delegate;
	private final String feature;

	public FeatureBoundRenderer(Renderer delegate, String feature) {
		super();
		this.delegate = delegate;
		this.feature = feature;
	}

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		Features.getInstance().featureBoundProcedure(feature, new Runnable() {
			@Override
			public void run() {
				delegate.render(gameContainer, graphics, renderingParameter);
			}
		});		
	}

}
