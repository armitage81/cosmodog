package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * This class aggregates all elementar sight radius calculators and construct the complex calculator based
 * on the game properties.
 */
public class GeneralSightRadiusCalculator implements SightRadiusCalculator {

	SightRadiusCalculator normalSightRadiusCalculator = new DayTimeAffectedSightRadiusCalculator(new DefaultSightRadiusCalculator());
	
	@Override
	public int calculateSightRadius(Enemy enemy, PlanetaryCalendar planetaryCalendar) {
		
		Player player = ApplicationContextUtils.getPlayer();
		CustomTiledMap map = ApplicationContextUtils.getCustomTiledMap();
		
		if (playerIsInHighGrassTile(map, player)) {
			return 1;
		} else {
			return normalSightRadiusCalculator.calculateSightRadius(enemy, planetaryCalendar);
		}
		
	}

	
	private boolean playerIsInHighGrassTile(CustomTiledMap map, Player player) {
		boolean retVal;
		int tileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_GROUNDTYPES);
		retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_PLANTS);
		
		return retVal;
	}
	
}
