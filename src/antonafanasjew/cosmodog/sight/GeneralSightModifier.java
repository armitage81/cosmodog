package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * This class aggregates all elementar sight radius calculators and construct the complex calculator based
 * on the game properties.
 */
public class GeneralSightModifier implements SightModifier {

	
	SightModifier normalSightModifier = new DayTimeSightModifier(new IdentitySightModifier());
	
	@Override
	public Sight modifySight(Sight sight, PlanetaryCalendar planetaryCalendar) {
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		if (playerIsInHighGrassTile(map, player)) {
			return sight.copyWithOtherDistance(DEFAULT_SIGHT_DISTANCE);
		} else {
			return normalSightModifier.modifySight(sight, planetaryCalendar);
		}
		
	}

	private boolean playerIsInHighGrassTile(CosmodogMap map, Player player) {
		boolean retVal;
		int tileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_GROUNDTYPES);
		retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_PLANTS);
		
		return retVal;
	}
	
}
