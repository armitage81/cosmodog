package antonafanasjew.cosmodog.util;

import java.util.List;
import java.util.Set;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.sight.Sight;
import antonafanasjew.cosmodog.sight.SightModifier;
import antonafanasjew.cosmodog.sight.VisibilityCalculator;
import antonafanasjew.cosmodog.topology.Position;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class CosmodogMapUtils {

	public static List<Enemy> enemiesAdjacentToPlayer(CosmodogMap map, Player player) {
		
		List<Enemy> retVal = Lists.newArrayList();
		
		int[] adjacentPositionsX = new int[]{player.getPositionX() - 1, player.getPositionX(), player.getPositionX(), player.getPositionX() + 1};
		int[] adjacentPositionsY = new int[]{player.getPositionY(), player.getPositionY() - 1, player.getPositionY() + 1, player.getPositionY()};
		
		Set<Enemy> enemies = map.getEnemies();
		for (Enemy enemy : enemies) {
			for (int i = 0; i < adjacentPositionsX.length; i++) {
				int adjacentX = adjacentPositionsX[i];
				int adjacentY = adjacentPositionsY[i];
				
				if (enemy.getPositionX() == adjacentX && enemy.getPositionY() == adjacentY) {

					//Artillery units should be excluded from adjacent enemies since they will be added
					//as adjacent ranged enemies anyway. It did not matter in the past, but since
					//artillery units have now an inner distance in which they cannot attack,
					//adding them to this list would be problematic since adjacent enemies always attack.
					//Actually, it should be valid for all units who have inner sight distance > 0.
					if (enemy.getSights().stream().anyMatch(e -> e.getInnerDistance() > 0)) {
						continue;
					}

					retVal.add(enemy);
				}
				
			}
		}
		
		return retVal;
	}
	
	public static List<Enemy> rangedEnemiesAdjacentToPlayer(CosmodogMap map, Player player) {
		
		List<Enemy> retVal = Lists.newArrayList();
		
		SightModifier sightModifier = ApplicationContextUtils.getCosmodog().getSightModifier();
		PlanetaryCalendar planetaryCalendar = ApplicationContextUtils.getCosmodogGame().getPlanetaryCalendar();
		
		Set<Enemy> enemies = map.getEnemies();
		for (Enemy enemy : enemies) {
		
			if (enemy.getUnitType().isRangedUnit() == false) {
				continue;
			}
			
			if (distanceBetweenPositions(enemy, player) > 50) {
				continue;
			}
			
			boolean playerInSightRange = false;
			
			Set<Sight> sights = enemy.getSights();

			for (Sight sight : sights) {
				Sight modifiedSight = sightModifier.modifySight(sight, planetaryCalendar);
				VisibilityCalculator visibilityCalculator = VisibilityCalculator.create(modifiedSight, enemy, map.getTileWidth(), map.getTileHeight());
				playerInSightRange = visibilityCalculator.visible(player, map.getTileWidth(), map.getTileHeight());
				if (playerInSightRange) {
					retVal.add(enemy);
				}
			}
		}
		
		return retVal;
	}
	
	public static float distanceBetweenPositions(Piece p1, Piece p2) {
		Position pos1 = Position.fromCoordinates(p1.getPositionX(), p1.getPositionY());
		Position pos2 = Position.fromCoordinates(p2.getPositionX(), p2.getPositionY());
		return distanceBetweenPositions(pos1, pos2);
	}
	
	public static float distanceBetweenPositions(Position p1, Position p2) {
		float dx = p1.getX() - p2.getX();
		float dy = p1.getY() - p2.getY();
		
		if (dx < 1) {
			dx = - dx;
		}
		
		if (dy < 1) {
			dy = - dy;
		}
		
		return dx + dy;
		
	}
	
	private static Set<Position> PLATFORMDATA = Sets.newHashSet();
	
	static {
		PLATFORMDATA.add(Position.fromCoordinates(-1, -3));
		PLATFORMDATA.add(Position.fromCoordinates( 3, -3));
		
		PLATFORMDATA.add(Position.fromCoordinates(-2, -2));
		PLATFORMDATA.add(Position.fromCoordinates(-1, -2));
		PLATFORMDATA.add(Position.fromCoordinates( 0, -2));
		PLATFORMDATA.add(Position.fromCoordinates( 1, -2));
		PLATFORMDATA.add(Position.fromCoordinates( 2, -2));
		PLATFORMDATA.add(Position.fromCoordinates( 3, -2));
		PLATFORMDATA.add(Position.fromCoordinates( 4, -2));
		
		PLATFORMDATA.add(Position.fromCoordinates(-3, -1));
		PLATFORMDATA.add(Position.fromCoordinates(-2, -1));
		PLATFORMDATA.add(Position.fromCoordinates(-1, -1));
		PLATFORMDATA.add(Position.fromCoordinates(-0, -1));
		PLATFORMDATA.add(Position.fromCoordinates( 1, -1));
		PLATFORMDATA.add(Position.fromCoordinates( 2, -1));
		PLATFORMDATA.add(Position.fromCoordinates( 3, -1));
		PLATFORMDATA.add(Position.fromCoordinates( 4, -1));
		PLATFORMDATA.add(Position.fromCoordinates( 5, -1));
		
		PLATFORMDATA.add(Position.fromCoordinates(-2, 0));
		PLATFORMDATA.add(Position.fromCoordinates(-1, 0));
		PLATFORMDATA.add(Position.fromCoordinates(-0, 0));
		PLATFORMDATA.add(Position.fromCoordinates( 1, 0));
		PLATFORMDATA.add(Position.fromCoordinates( 2, 0));
		PLATFORMDATA.add(Position.fromCoordinates( 3, 0));
		PLATFORMDATA.add(Position.fromCoordinates( 4, 0));
		
		PLATFORMDATA.add(Position.fromCoordinates(-2, 1));
		PLATFORMDATA.add(Position.fromCoordinates(-1, 1));
		PLATFORMDATA.add(Position.fromCoordinates(-0, 1));
		PLATFORMDATA.add(Position.fromCoordinates( 1, 1));
		PLATFORMDATA.add(Position.fromCoordinates( 2, 1));
		PLATFORMDATA.add(Position.fromCoordinates( 3, 1));
		PLATFORMDATA.add(Position.fromCoordinates( 4, 1));
		
		PLATFORMDATA.add(Position.fromCoordinates(-2, 2));
		PLATFORMDATA.add(Position.fromCoordinates(-1, 2));
		PLATFORMDATA.add(Position.fromCoordinates(-0, 2));
		PLATFORMDATA.add(Position.fromCoordinates( 1, 2));
		PLATFORMDATA.add(Position.fromCoordinates( 2, 2));
		PLATFORMDATA.add(Position.fromCoordinates( 3, 2));
		PLATFORMDATA.add(Position.fromCoordinates( 4, 2));
		
		PLATFORMDATA.add(Position.fromCoordinates(-1, 3));
		PLATFORMDATA.add(Position.fromCoordinates( 1, 3));
		PLATFORMDATA.add(Position.fromCoordinates( 2, 3));
		PLATFORMDATA.add(Position.fromCoordinates( 3, 3));
	}
	
	/**
	 * Use this to define whether the target tile is part of the platform or not. 
	 */
	public static boolean isTileOnPlatform(int tileX, int tileY) {
		boolean retVal = false;
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Platform platform = cosmodogGame.getMap().getCachedPlatform(cosmodogGame);
		if (platform != null) {
			return isTileOnPlatform(tileX, tileY, platform.getPositionX(), platform.getPositionY());
		}
		return retVal;
	}
	
	public static boolean isTileOnPlatform(int tileX, int tileY, int platformX, int platformY) {
		boolean retVal = false;
		int actorOffsetX = tileX - platformX;
		int actorOffsetY = tileY - platformY;
		Position offsetPosition = Position.fromCoordinates(actorOffsetX, actorOffsetY);
		retVal = PLATFORMDATA.contains(offsetPosition);
		return retVal;
	}
	
}
