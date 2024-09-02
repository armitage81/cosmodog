package antonafanasjew.cosmodog.pathfinding;

import java.util.List;
import java.util.Random;

import antonafanasjew.cosmodog.globals.Constants;
import org.newdawn.slick.util.pathfinding.Path;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import com.google.common.collect.Lists;

/**
 * Random path finder for testing. The enemy will move one tile randomly each turn.
 */
public class RandomPathFinder extends AbstractPathFinder {

	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor enemy, int costBudget, CollisionValidator collisionValidator, MovementActionResult playerMovementActionResult) {
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		//Throw a number from 0 to 3. Select the according step (up, down, left, right) and cycle through all 4 directions starting with the thrown index till the collision validator accepts it (if not, just do not move).
		int xSteps[] = new int[] {enemy.getPositionX(), enemy.getPositionX(), enemy.getPositionX() - 1, enemy.getPositionX() + 1};
		int ySteps[] = new int[] {enemy.getPositionY() + 1, enemy.getPositionY() - 1, enemy.getPositionY(), enemy.getPositionY()};
		
		int x = -1;
		int y = -1;

		
		
		Random r = new Random();
		
		//Move randomly only in 50% of cases. Stay still in remaining cases.
		boolean shouldMove = r.nextBoolean();
		
		if (shouldMove) {
		
			int firstIndex = r.nextInt();
			if (firstIndex < 0) {
				firstIndex = - firstIndex;
			}
			
			firstIndex = firstIndex % 4;
			
			
			
			for (int i = 0; i < 4; i++) {
				int index = (firstIndex + i) % 4;
				int xAtIndex = xSteps[index];
				int yAtIndex = ySteps[index];
				
				CollisionStatus collisionStatus = collisionValidator.collisionStatus(game, enemy, map, xAtIndex, yAtIndex);
				
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
    		path.appendStep(enemy.getPositionX(), enemy.getPositionY());
    		path.appendStep(x, y);
    		retVal = MovementActionResult.instance(path);
		
		}
		
		return retVal;
	}

}
