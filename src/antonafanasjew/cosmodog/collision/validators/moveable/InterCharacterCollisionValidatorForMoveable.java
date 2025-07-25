package antonafanasjew.cosmodog.collision.validators.moveable;

import java.util.Set;

import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;
import com.google.common.collect.Sets;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;

/**
 * This validator is for moveables only. It checks whether the target tile is occupied by an NPC. If yes, there is a collision.
 * 
 * Since the player has the priority while moving, the current position of enemies is taken into account, not the target location.
 */
public class InterCharacterCollisionValidatorForMoveable extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		CosmodogMap cosmodogMap = cosmodogGame.mapOfPlayerLocation();
		Set<Enemy> enemies = cosmodogMap.getEnemiesInRange();
		
		Set<Actor> allActors = Sets.newHashSet();
		allActors.addAll(enemies);
		allActors.remove(actor); //Don't check for the actor himself
		
		for (Actor oneActor : allActors) {
			
			Position blockedPos = oneActor.getPosition();

			if (blockedPos.equals(entrance.getPosition())) {
				return CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_AS_TARGET_BY_OTHER_MOVING_CHARACTER);
			}
		}
		return CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);
	}

}
