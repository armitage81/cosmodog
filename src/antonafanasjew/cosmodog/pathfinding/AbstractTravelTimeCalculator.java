package antonafanasjew.cosmodog.pathfinding;

import java.util.concurrent.Callable;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.actors.Actor;

public abstract class AbstractTravelTimeCalculator implements TravelTimeCalculator {

	@Override
	public int calculateTravelTime(ApplicationContext context, Actor actor, int x, int y) {
		return Features.getInstance().featureBoundFunction(Features.FEATURE_MOVEMENT_COSTS, new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return calculateTravelTimeInternal(context, actor, x, y);
			}
			
		},
		Constants.MINIMAL_TIME_UNIT_IN_MINUTES); 
				
	}

	protected abstract int calculateTravelTimeInternal(ApplicationContext context, Actor actor, int x, int y);

}
