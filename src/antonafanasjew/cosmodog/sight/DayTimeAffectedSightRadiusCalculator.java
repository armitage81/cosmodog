package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.model.actors.Enemy;

public class DayTimeAffectedSightRadiusCalculator implements SightRadiusCalculator {

	private SightRadiusCalculator underlyingSightRadiusCalculator;
	
	public DayTimeAffectedSightRadiusCalculator(SightRadiusCalculator underlyingSightRadiusCalculator) {
		this.underlyingSightRadiusCalculator = underlyingSightRadiusCalculator;
	}
	
	@Override
	public int calculateSightRadius(Enemy enemy, PlanetaryCalendar planetaryCalendar) {
		int retVal = underlyingSightRadiusCalculator.calculateSightRadius(enemy, planetaryCalendar);
		if (planetaryCalendar.isNight() && retVal > 1) {
			retVal -= 1;
		}
		return retVal;
	}

}
