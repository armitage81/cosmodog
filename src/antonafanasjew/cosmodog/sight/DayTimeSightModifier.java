package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;

public class DayTimeSightModifier implements SightModifier {

	private SightModifier underlyingSightRadiusCalculator;
	
	public DayTimeSightModifier(SightModifier underlyingSightRadiusCalculator) {
		this.underlyingSightRadiusCalculator = underlyingSightRadiusCalculator;
	}
	
	@Override
	public Sight modifySight(Sight sight, PlanetaryCalendar planetaryCalendar) {
		Sight retVal = underlyingSightRadiusCalculator.modifySight(sight, planetaryCalendar);
		float distance = retVal.getDistance();
		if (planetaryCalendar.isNight() && distance >= DEFAULT_SIGHT_DISTANCE * 2) {
			retVal = retVal.copyWithOtherDistance(distance - DEFAULT_SIGHT_DISTANCE);
		}
		return retVal;
	}

}
