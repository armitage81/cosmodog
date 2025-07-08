package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import profiling.ProfilerUtils;
import profiling.ProfilingData;
import profiling.ProfilingEntry;

public abstract class AbstractRenderer implements Renderer {

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public final void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		ProfilerUtils.runWithProfiling(this.getClass().getSimpleName(), () -> {
			renderInternally(gameContainer, graphics, renderingParameter);
		});
	}

	protected abstract void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter);


}
