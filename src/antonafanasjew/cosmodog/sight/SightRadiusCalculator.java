package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.model.actors.Enemy;

public interface SightRadiusCalculator {

	int calculateSightRadius(Enemy enemy, PlanetaryCalendar planetaryCalendar);
	
}
