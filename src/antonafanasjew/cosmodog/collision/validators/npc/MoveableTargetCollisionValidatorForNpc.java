package antonafanasjew.cosmodog.collision.validators.npc;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerDescriptor;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

/**
 * When a moveable is being moved, its target must not be accessible by an npc.
 */
public class MoveableTargetCollisionValidatorForNpc extends AbstractCollisionValidator {

	private final MovementActionResult moveableActionResult;

	public MoveableTargetCollisionValidatorForNpc(MovementActionResult moveableActionResult) {
		this.moveableActionResult = moveableActionResult;
	}
	
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, position, true, PassageBlockerDescriptor.fromPassageBlockerType(PassageBlockerType.PASSABLE));
		if (moveableActionResult != null) {
			int moveableTargetX = moveableActionResult.getPath().getX(1);
			int moveableTargetY = moveableActionResult.getPath().getY(1);
			Position moveableTargetPosition = Position.fromCoordinates(moveableTargetX, moveableTargetY);
			if (moveableTargetPosition.equals(position)) {
				retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerDescriptor.fromPassageBlockerType(PassageBlockerType.BLOCKED_DYNAMIC_PIECE));
			}
		}
		return retVal;
	}
	
}
