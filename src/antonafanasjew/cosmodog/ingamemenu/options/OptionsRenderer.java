package antonafanasjew.cosmodog.ingamemenu.options;



import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class OptionsRenderer implements Renderer {

	private static final String TEXT = "Press [RETURN] to save and quit";
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, drawingContext, TEXT, FontType.InGameMenuInterface);
	}

}
