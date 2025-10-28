package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.function.Supplier;

//This is a decorator that uses an underlying renderer if the given condition is true.
public class ConditionalRenderer extends AbstractRenderer {

    private Supplier<Boolean> condition;
    private Renderer delegate;
    private Renderer alternativeDelegate;

    @Override
    public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

        if (condition.get()) {
            delegate.render(gameContainer, graphics, renderingParameter);
        } else {
            if (alternativeDelegate != null) {
                alternativeDelegate.render(gameContainer, graphics, renderingParameter);
            }
        }

    }

    public static ConditionalRenderer instance(Renderer delegate, Renderer alternativeDelegate, Supplier<Boolean> condition) {
        ConditionalRenderer cr = new ConditionalRenderer();
        cr.delegate = delegate;
        cr.alternativeDelegate = alternativeDelegate;
        cr.condition = condition;
        return cr;
    }

    public static ConditionalRenderer instanceWithSkyDecorationsActiveCondition(Renderer delegate) {
        return instance(
                delegate,
                null,
                () -> ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation().getMapDescriptor().isSkyDecorationsActive()
        );
    }

    public static ConditionalRenderer instanceWithDayNightActiveCondition(Renderer delegate) {
        return instanceWithDayNightActiveConditionAndAlternative(
                delegate,
                null
        );
    }

    public static ConditionalRenderer instanceWithDayNightActiveConditionAndAlternative(Renderer delegate, Renderer alternativeDelegate) {
        return instance(
                delegate,
                alternativeDelegate,
                () -> ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation().getMapDescriptor().isDayNightActive()
        );
    }

    public static ConditionalRenderer instanceWithResourceConsumptionActiveConditionAndAlternative(Renderer delegate, Renderer alternativeDelegate) {
        return instance(
                delegate,
                alternativeDelegate,
                () -> ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation().getMapDescriptor().isResourceConsumptionActive()
        );
    }

}
