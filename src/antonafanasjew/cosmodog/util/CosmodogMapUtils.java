package antonafanasjew.cosmodog.util;

import java.util.List;
import java.util.Set;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
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
					retVal.add(enemy);
				}
				
			}
		}
		
		return retVal;
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
