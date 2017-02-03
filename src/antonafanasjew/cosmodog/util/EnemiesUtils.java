package antonafanasjew.cosmodog.util;

import java.util.Iterator;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.model.actors.Enemy;

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
		return !(enemy.isActiveAtDayTimeOnly() && calendar.isNight());
	}
	
}
