package antonafanasjew.cosmodog.util;

import java.util.List;
import java.util.Set;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;

import com.google.common.collect.Lists;

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
	
}
