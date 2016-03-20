package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.model.actors.Enemy;

public class DefaultSightRadiusCalculator implements SightRadiusCalculator {

	@Override
	public int calculateSightRadius(Enemy enemy, PlanetaryCalendar planetaryCalendar) {
		return enemy.getSightRadius();
	}

}
