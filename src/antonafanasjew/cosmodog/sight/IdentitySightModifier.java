package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;

public class IdentitySightModifier implements SightModifier {

	@Override
	public Sight modifySight(Sight sight, PlanetaryCalendar planetaryCalendar) {
		return sight.copyWithOtherDistance(sight.getDistance());
	}

}
