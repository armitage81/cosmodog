package antonafanasjew.cosmodog.collision.validators.player;

import java.util.Set;

import antonafanasjew.cosmodog.topology.Position;
import com.google.common.collect.Sets;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

/**
 * This validator is for the player only. It checks whether the target tile is occupied by an NPC. If yes, there is a collision.
 * 
 * Since the player has the priority while moving, the current position of enemies is taken into account, not the target location.
 */
public class InterCharacterCollisionValidatorForPlayer extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		Player player = cosmodogGame.getPlayer();
		Set<Enemy> enemies = map.getEnemiesInRange();
		
		Set<Actor> allActors = Sets.newHashSet();
		allActors.add(player);
		allActors.addAll(enemies);
		allActors.remove(actor); //Don't check for the actor himself
		
		for (Actor oneActor : allActors) {
			
			Position blockedPosition = oneActor.getPosition();

			if (blockedPosition.equals(position)) {
				return CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_AS_TARGET_BY_OTHER_MOVING_CHARACTER);
			}
		}
		return CollisionStatus.instance(actor, map, position, true, PassageBlockerType.PASSABLE);
	}

}
