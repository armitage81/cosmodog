package antonafanasjew.cosmodog.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.sight.VisibilityCalculator;
import antonafanasjew.cosmodog.topology.Position;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class CosmodogMapUtils {

	public static List<Enemy> enemiesAdjacentToPlayer(CosmodogMap map, Player player) {
		
		List<Enemy> retVal = Lists.newArrayList();
		
		Position[] adjacentPositions = new Position[] {
				player.getPosition().shifted(-1, 0),
				player.getPosition().shifted(0, -1),
				player.getPosition().shifted(0, 1),
				player.getPosition().shifted(1, 0)
		};

		Set<Enemy> enemies = map.allEnemies();
		for (Enemy enemy : enemies) {
            for (Position adjacentPosition : adjacentPositions) {

                if (enemy.getPosition().equals(adjacentPosition)) {

                    //Artillery units should be excluded from adjacent enemies since they will be added
                    //as adjacent ranged enemies anyway. It did not matter in the past, but since
                    //artillery units have now an inner distance in which they cannot attack,
                    //adding them to this list would be problematic since adjacent enemies always attack.
                    //Actually, it should be valid for all units who have inner sight distance > 0.
                    if (enemy.getUnitType() == UnitType.ARTILLERY) {
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
		
		PlanetaryCalendar planetaryCalendar = ApplicationContextUtils.getCosmodogGame().getPlanetaryCalendar();
		
		Set<Enemy> enemies = map.allEnemies();
		for (Enemy enemy : enemies) {
		
			if (!enemy.getUnitType().isRangedUnit()) {
				continue;
			}
			
			if (distanceBetweenPositions(enemy, player) > 50) {
				continue;
			}
			
			boolean playerInSightRange = false;
			

			VisibilityCalculator visibilityCalculator = VisibilityCalculator.create(enemy.getDefaultVision(), enemy.getNightVision(), enemy.getStealthVision());
			playerInSightRange = visibilityCalculator.visible(enemy, planetaryCalendar, map, player);
			if (playerInSightRange) {
				retVal.add(enemy);
			}
		}
		
		return retVal;
	}
	
	public static float distanceBetweenPositions(Piece p1, Piece p2) {
		Position pos1 = p1.getPosition();
		Position pos2 = p2.getPosition();
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
	
	private static final Set<Position> PLATFORMDATA = Sets.newHashSet();
	
	static {
		PLATFORMDATA.add(Position.fromCoordinates(-1, -3, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 3, -3, ApplicationContextUtils.mapDescriptorMain()));
		
		PLATFORMDATA.add(Position.fromCoordinates(-2, -2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates(-1, -2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 0, -2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 1, -2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 2, -2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 3, -2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 4, -2, ApplicationContextUtils.mapDescriptorMain()));
		
		PLATFORMDATA.add(Position.fromCoordinates(-3, -1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates(-2, -1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates(-1, -1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates(-0, -1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 1, -1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 2, -1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 3, -1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 4, -1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 5, -1, ApplicationContextUtils.mapDescriptorMain()));
		
		PLATFORMDATA.add(Position.fromCoordinates(-2, 0, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates(-1, 0, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates(-0, 0, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 1, 0, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 2, 0, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 3, 0, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 4, 0, ApplicationContextUtils.mapDescriptorMain()));
		
		PLATFORMDATA.add(Position.fromCoordinates(-2, 1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates(-1, 1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates(-0, 1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 1, 1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 2, 1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 3, 1, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 4, 1, ApplicationContextUtils.mapDescriptorMain()));
		
		PLATFORMDATA.add(Position.fromCoordinates(-2, 2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates(-1, 2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates(-0, 2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 1, 2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 2, 2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 3, 2, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 4, 2, ApplicationContextUtils.mapDescriptorMain()));
		
		PLATFORMDATA.add(Position.fromCoordinates(-1, 3, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 1, 3, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 2, 3, ApplicationContextUtils.mapDescriptorMain()));
		PLATFORMDATA.add(Position.fromCoordinates( 3, 3, ApplicationContextUtils.mapDescriptorMain()));
	}

	/**
	 * Use this to define whether the target tile is part of the platform or not. 
	 */
	public static boolean isTileOnPlatform(Position tilePosition) {
		boolean retVal = false;
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Set<Platform> platforms = cosmodogGame.getMaps().get(tilePosition.getMapDescriptor()).getPlatforms();
		for (Platform platform : platforms) {
			if (isTileOnPlatform(tilePosition, platform.getPosition())) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}
	
	public static boolean isTileOnPlatform(Position tilePosition, Position platformPosition) {
		boolean retVal = false;
		if (tilePosition.getMapDescriptor().equals(platformPosition.getMapDescriptor())) {
			int actorOffsetX = (int) (tilePosition.getX() - platformPosition.getX());
			int actorOffsetY = (int) (tilePosition.getY() - platformPosition.getY());
			Position offsetPosition = Position.fromCoordinates(actorOffsetX, actorOffsetY, ApplicationContextUtils.mapDescriptorMain());
			retVal = PLATFORMDATA.contains(offsetPosition);
		}
		return retVal;
	}

	public static Set<Position> positionsCoveredByPlatform(Platform platform) {
		Set<Position> retVal = new HashSet<>();
		for (Position offsetPosition : PLATFORMDATA) {
			float x = platform.getPosition().getX() + offsetPosition.getX();
			float y = platform.getPosition().getY() + offsetPosition.getY();
			Position position = Position.fromCoordinates(x, y, platform.getPosition().getMapDescriptor());
			retVal.add(position);
		}
		return retVal;
	}
	
}
