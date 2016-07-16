package antonafanasjew.cosmodog.calendar.listeners;

import antonafanasjew.cosmodog.calendar.AbstractPlanetaryCalendarListener;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Calendar listener that consumes players water for each time update on the calendar.
 */
public class WaterConsumer extends AbstractPlanetaryCalendarListener {

	private static final long serialVersionUID = 6229567298447393618L;

	/**
	 * Consumes players water (increases thirst) if the thirst feature is activated depending on the passed time. 
	 */
	@Override
	protected void timePassedInternal(long fromTimeInMinutes, int noOfMinutes) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_THIRST, new Runnable() {
			@Override
			public void run() {
				Player player = ApplicationContextUtils.getPlayer();
				
				int timeUnits = noOfMinutes / Constants.MINIMAL_TIME_UNIT_IN_MINUTES;				
				player.decreaseWater(timeUnits);
			}
		});
	}

}
