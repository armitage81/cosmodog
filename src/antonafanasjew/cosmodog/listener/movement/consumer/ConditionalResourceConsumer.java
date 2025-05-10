package antonafanasjew.cosmodog.listener.movement.consumer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.renderer.ConditionalRenderer;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.util.function.Supplier;

public class ConditionalResourceConsumer implements ResourceConsumer {

    private Supplier<Boolean> condition;
    private ResourceConsumer delegate;
    private ResourceConsumer alternativeDelegate;

    @Override
    public int turnCosts(Position position1, Position position2, Player player, CosmodogMap map, ApplicationContext cx) {
        return condition.get() ? delegate.turnCosts(position1, position2, player, map, cx) : alternativeDelegate.turnCosts(position1, position2, player, map, cx);
    }

    public static ConditionalResourceConsumer instance(ResourceConsumer delegate, ResourceConsumer alternativeDelegate, Supplier<Boolean> condition) {
        ConditionalResourceConsumer cr = new ConditionalResourceConsumer();
        cr.delegate = delegate;
        cr.alternativeDelegate = alternativeDelegate;
        cr.condition = condition;
        return cr;
    }

    public static ConditionalResourceConsumer instanceWithResourceConsumptionActiveCondition(ResourceConsumer delegate) {
        return instance(
                delegate,
                new ResourceConsumer() {
                    @Override
                    public int turnCosts(Position position1, Position position2, Player player, CosmodogMap map, ApplicationContext cx) {
                        return 0;
                    }
                },
                () -> ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation().getMapType().isResourceConsumptionActive()
        );
    }
}
