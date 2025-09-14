package antonafanasjew.cosmodog.util;

import java.util.Iterator;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.structures.Race;

public class EnemiesUtils {
	
	public static void removeInactiveUnits(Iterable<Enemy> enemies) {
		
		Iterator<Enemy> it = enemies.iterator();
		while (it.hasNext()) {
			Enemy enemy = it.next();
			if (!enemyActive(enemy)) {
				it.remove();
			}
		}
	}

	public static boolean enemyActive(Enemy enemy) {
		PlanetaryCalendar calendar = ApplicationContextUtils.getCosmodogGame().getPlanetaryCalendar();	
		Race race = PlayerMovementCache.getInstance().getActiveRace();


		boolean playerRacing = race != null && race.isStarted() && !race.isSolved();
		boolean playerInSokobanPuzzle = PlayerMovementCache.getInstance().getActiveMoveableGroup() != null;
		boolean playerInSafeSpace = PlayerMovementCache.getInstance().getActiveSafeSpace() != null;
		boolean playerInPortalPuzzle = PlayerMovementCache.getInstance().getActivePortalPuzzle() != null;
		boolean solarByNight = (enemy.isActiveAtDayTimeOnly() && calendar.isNight());

		return !(playerRacing || playerInSokobanPuzzle || playerInSafeSpace || playerInPortalPuzzle || solarByNight);

	}
	
}
