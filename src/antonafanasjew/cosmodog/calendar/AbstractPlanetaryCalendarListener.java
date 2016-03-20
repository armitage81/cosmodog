package antonafanasjew.cosmodog.calendar;

/**
 * Abstract implementation of {@link PlanetaryCalendarListener}
 * 
 */
public abstract class AbstractPlanetaryCalendarListener implements PlanetaryCalendarListener {

	private static final long serialVersionUID = -6011130727726921032L;

	@Override
	public void timePassed(long fromTimeInMinutes, int noOfMinutes) {
		timePassedInternal(fromTimeInMinutes, noOfMinutes);
	}

	protected abstract void timePassedInternal(long fromTimeInMinutes, int noOfMinutes);
	
}
