package antonafanasjew.cosmodog.calendar;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Defines a way to aggregate multiple calendar listeners in one.
 */
public class ComposedPlanetaryCalendarListener implements PlanetaryCalendarListener {

	private static final long serialVersionUID = 2472622693306534082L;

	private List<PlanetaryCalendarListener> underlyings = Lists.newArrayList();
	
	/**
	 * Executes all underlying listeners in a row.
	 */
	@Override
	public void timePassed(long fromTimeInMinutes, int noOfMinutes) {
		for (PlanetaryCalendarListener underlying : underlyings) {
			underlying.timePassed(fromTimeInMinutes, noOfMinutes);
		}
	}

	/**
	 * Returns the underlying listeners. That's the only way to add a new one. 
	 * @return List of underlying listeners in the order of the execution.
	 */
	public List<PlanetaryCalendarListener> getUnderlyings() {
		return underlyings;
	}

}
