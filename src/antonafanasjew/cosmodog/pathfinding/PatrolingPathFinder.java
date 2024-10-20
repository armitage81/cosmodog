package antonafanasjew.cosmodog.pathfinding;

import java.util.List;
import java.util.Map;
import java.util.Random;

import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.util.pathfinding.Path;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Patroling path finder. Actors will randomly change their direction but will prefer to go strait. 
 */
public class PatrolingPathFinder extends AbstractPathFinder {

	private static final int CHANCE_TO_MOVE_IN_THE_SAME_DIRECTION = 5;

	private static final Map<DirectionType, Integer> DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY = Maps.newHashMap();
	static {
		DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY.put(DirectionType.DOWN, 0);
		DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY.put(DirectionType.UP, 1);
		DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY.put(DirectionType.LEFT, 2);
		DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY.put(DirectionType.RIGHT, 3);
	}

	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor enemy, int costBudget, CollisionValidator collisionValidator, MovementActionResult playerMovementActionResult) {
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		//Throw a number from 0 to 3. Select the according step (up, down, left, right) and cycle through all 4 directions starting with the thrown index till the collision validator accepts it (if not, just do not move).
		float[] xSteps = new float[] {enemy.getPosition().getX(), enemy.getPosition().getX(), enemy.getPosition().getX() - 1, enemy.getPosition().getX() + 1};
		float[] ySteps = new float[] {enemy.getPosition().getY() + 1, enemy.getPosition().getY() - 1, enemy.getPosition().getY(), enemy.getPosition().getY()};
		
		float x = -1;
		float y = -1;
		
		int firstIndex;
		
		//Before randomly choosing a direction, give a 75% chance to move in the same direction.
		Random r = new Random();
		
		boolean shouldMove = r.nextBoolean();
		
		if (shouldMove) {
		
			int diceThrow = r.nextInt();
			if (diceThrow < 0) {
				diceThrow = - diceThrow;
			}
			
			boolean continueMovingInSameDirection = diceThrow % CHANCE_TO_MOVE_IN_THE_SAME_DIRECTION != 0;
			
			if (continueMovingInSameDirection) {
				firstIndex = DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY.get(enemy.getDirection());
			} else {
			
				r = new Random();
				firstIndex = r.nextInt();
				if (firstIndex < 0) {
					firstIndex = - firstIndex;
				}
				
				firstIndex = firstIndex % 4;
			
			}
			
			for (int i = 0; i < 4; i++) {
				int index = (firstIndex + i) % 4;
				float xAtIndex = xSteps[index];
				float yAtIndex = ySteps[index];
				
				CollisionStatus collisionStatus = collisionValidator.collisionStatus(game, enemy, map, Position.fromCoordinates(xAtIndex, yAtIndex));
				
				if (collisionStatus.isPassable()) {
					x = xAtIndex;
					y = yAtIndex;
					break;
				}
			}
		}
		
		MovementActionResult retVal = null;
		
		if (x != -1 && y != -1) {
			
    		Path path = new Path();
    		path.appendStep((int)enemy.getPosition().getX(), (int)enemy.getPosition().getY());
    		path.appendStep((int)x, (int)y);
    		retVal = MovementActionResult.instance(path);
		
		}
		
		return retVal;
	}

}
