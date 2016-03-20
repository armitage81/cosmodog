package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Defines the travel time calculation method to enter a tile.
 */
public interface TravelTimeCalculator {

	int calculateTravelTime(ApplicationContext context, Actor actor, int x, int y);
	
}
