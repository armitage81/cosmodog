package antonafanasjew.cosmodog.calendar.listeners;

import antonafanasjew.cosmodog.calendar.AbstractPlanetaryCalendarListener;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Calendar listener that consumes players food for each time update on the calendar.
 */
public class FoodConsumer extends AbstractPlanetaryCalendarListener {

	private static final long serialVersionUID = -8268197185736624543L;

	/**
	 * Consumes players food (increases hunger) if the hunger feature is activated depending on the passed time. 
	 */
	@Override
	protected void timePassedInternal(long fromTimeInMinutes, int noOfMinutes) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_HUNGER, new Runnable() {
			@Override
			public void run() {
				Player player = ApplicationContextUtils.getPlayer();
				int timeUnits = noOfMinutes / Constants.MINIMAL_TIME_UNIT_IN_MINUTES;
				player.increaseHunger(timeUnits);
			}
		});
	}

}
