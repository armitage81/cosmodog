package antonafanasjew.cosmodog.listener.movement.consumer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;

/**
 * As consumption of resources (water, food, fuel) is more complex now,
 * this interface has been created.
 * Implementations can define, how a resource is consumed when passing a turn.
 * (e.g. water is consumed more in deserts, fuel is consumed less on roads, etc.)
 */
public interface ResourceConsumer {

	int turnCosts(Position position1, Position position2, Player player, CosmodogMap map, ApplicationContext cx);
	
}
