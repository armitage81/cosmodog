package antonafanasjew.cosmodog.pathfinding;

import java.util.List;

import org.newdawn.slick.util.pathfinding.Path;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.actors.Actor;

import com.google.common.collect.Lists;

public class StayStillPathFinder extends AbstractPathFinder {

	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, MovementActionResult playerMovementActionResult) {
		
		//Adding the actors position as the first step in the path in any case.
		Path subPath = new Path();
		subPath.appendStep(actor.getPositionX(), actor.getPositionY());
		List<Float> costs = Lists.newArrayList();
		MovementActionResult retVal = MovementActionResult.instance(subPath, costs);
		return retVal;
        
	}
	

}
