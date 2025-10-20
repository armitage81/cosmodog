package antonafanasjew.cosmodog.util;

import java.util.Iterator;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.structures.SafeSpace;

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

		Player player = ApplicationContextUtils.getPlayer();
		PlanetaryCalendar calendar = ApplicationContextUtils.getCosmodogGame().getPlanetaryCalendar();
		Race race = PlayerMovementCache.getInstance().getActiveRace();


		boolean playerRacing = race != null && race.isStarted() && !race.isSolved();

		MoveableGroup moveableGroup = PlayerMovementCache.getInstance().getActiveMoveableGroup();

		boolean playerInMoveableGroupWithEnemyDeactivationFlagSet = moveableGroup != null && moveableGroup.isDeactivateEnemies();
		SafeSpace safeSpaceAroundPlayer = PlayerMovementCache.getInstance().getActiveSafeSpace();

		boolean playerIsInSafeSpaceAndEnemyIsNot = safeSpaceAroundPlayer != null && !RegionUtils.pieceInRegion(enemy, player.getPosition().getMapType(), safeSpaceAroundPlayer.getRegion());
		boolean playerInPortalPuzzle = PlayerMovementCache.getInstance().getActivePortalPuzzle() != null;
		boolean solarByNight = (enemy.isActiveAtDayTimeOnly() && calendar.isNight());

		return !(playerRacing || playerInMoveableGroupWithEnemyDeactivationFlagSet || playerIsInSafeSpaceAndEnemyIsNot || playerInPortalPuzzle || solarByNight);

	}
	
}
