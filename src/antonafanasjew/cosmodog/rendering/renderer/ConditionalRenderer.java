package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.function.Supplier;

//This is a decorator that uses an underlying renderer if the given condition is true.
public class ConditionalRenderer extends AbstractRenderer {

    private Supplier<Boolean> condition;
    private Renderer delegate;

    @Override
    public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

        if (condition.get()) {
            delegate.render(gameContainer, graphics, renderingParameter);
        }

    }

    public static ConditionalRenderer instance(Renderer delegate, Supplier<Boolean> condition) {
        ConditionalRenderer cr = new ConditionalRenderer();
        cr.delegate = delegate;
        cr.condition = condition;
        return cr;
    }

    public static ConditionalRenderer instanceWithSkyDecorationsActiveCondition(Renderer delegate) {
        return instance(
                delegate,
                () -> ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation().getMapType().isSkyDecorationsActive()
        );


    }

}
