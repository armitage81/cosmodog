package antonafanasjew.cosmodog.pathfinding;

import java.util.List;

import antonafanasjew.cosmodog.model.portals.Entrance;
import org.newdawn.slick.util.pathfinding.Path;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.actors.Actor;

import com.google.common.collect.Lists;

public class StayStillPathFinder extends AbstractPathFinder {

	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance) {
		
		//Adding the actors position as the first step in the path in any case.
		Path subPath = new Path();
		subPath.appendStep((int)actor.getPosition().getX(), (int)actor.getPosition().getY());
		MovementActionResult retVal = MovementActionResult.instance(subPath);
		return retVal;
        
	}
	

}
