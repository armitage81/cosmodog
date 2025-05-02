package antonafanasjew.cosmodog.rendering.renderer.textbook;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class BackgroundRenderer extends AbstractRenderer {
    @Override
    public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
        Image image = ApplicationContext.instance().getImages().get("space.background");
        image.draw(0, 0, gameContainer.getWidth(), gameContainer.getHeight());
    }
}
