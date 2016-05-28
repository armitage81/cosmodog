package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;

public interface SightModifier {

	int DEFAULT_SIGHT_DISTANCE = 16;
	
	Sight modifySight(Sight sight, PlanetaryCalendar planetaryCalendar);
	
}
