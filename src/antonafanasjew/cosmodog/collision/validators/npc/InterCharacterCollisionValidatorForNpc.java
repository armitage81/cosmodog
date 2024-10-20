package antonafanasjew.cosmodog.collision.validators.npc;

import java.util.Map;
import java.util.Set;

import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

import com.google.common.collect.Sets;

/**
 * This validator defines collision status in between the game characters. It will mark the tile as not passable
 * in case it was already chosen by another character as target. This validator should be used in movement actions
 * to prioritize the targets of the characters.
 * 
 * Take note: This validator will be mostly called at the beginning of movement actions to define whether the target tile of an NPC is free of other characters,
 * but it will not concern itself with the actual character positions, but with their target positions.
 * 
 * Example: Player wants to move from 0/0 to 0/1. During the movement action, his position will be still 0/0. 0/1 will be stored as players target.
 * After triggering the movement action, three nearby enemies want to move as following:
 * 
 * Enemy1: 0/0 => Will work, even if the player is still positioned there, as he will shift after the action will finish
 * Enemy2: 0/1 => Will not work, even if the tile is free till the players movement action finishes, because this tile is players target.
 * Enemy3: 0/0 => Will not work, because enemy 1 has already successfully chosen this tile as his target (even if he is not positioned there till the movement action finishes)
 * 
 * Take note: The movement result of the actual character for whom the validator will be called must not be in the map of movement action results. In case of the player,
 * his movement action result must be guaranteed as being null.
 * 
 * Take note: Sometimes, characters do not move. There movement action results will be non-existent. In this case, the validator will block tiles at such characters positions.
 * 
 */
public class InterCharacterCollisionValidatorForNpc extends AbstractCollisionValidator {

	private MovementActionResult playerMovementActionResult;
	private Map<Enemy, MovementActionResult> enemyMovementActionResults;
	
	/**
	 * Initialized with movement results of all actors.
	 * @param playerMovementActionResult Player movement result.
	 * @param enemyMovementActionResults Enemies movement results.
	 */
	public InterCharacterCollisionValidatorForNpc(MovementActionResult playerMovementActionResult, Map<Enemy, MovementActionResult> enemyMovementActionResults) {
		this.playerMovementActionResult = playerMovementActionResult;
		this.enemyMovementActionResults = enemyMovementActionResults;
	}
	
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		CosmodogMap cosmodogMap = cosmodogGame.mapOfPlayerLocation();
		Player player = cosmodogGame.getPlayer();
		Set<Enemy> enemies = cosmodogMap.getEnemiesInRange();
		
		Set<Actor> allActors = Sets.newHashSet();
		allActors.add(player);
		allActors.addAll(enemies);
		allActors.remove(actor); //Don't check for the actor himself
		
		for (Actor oneActor : allActors) {
			
			MovementActionResult oneActorsMovementActionResult = (oneActor instanceof Player) ? playerMovementActionResult : enemyMovementActionResults.get(oneActor);
			
			Position blockedPos;

			if (oneActorsMovementActionResult == null) { //Actor is not moving, so his position tile is blocked.
				blockedPos = oneActor.getPosition();
			} else { //Actor is moving, so his target tile is blocked.
				Path oneActorsPath = oneActorsMovementActionResult.getPath();
				Step oneActorsLastStep = oneActorsPath.getStep(oneActorsPath.getLength() - 1);
				blockedPos = Position.fromCoordinates(oneActorsLastStep.getX(), oneActorsLastStep.getY());
			}
			
			
			if (blockedPos.equals(position)) {
				return CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_AS_TARGET_BY_OTHER_MOVING_CHARACTER);
			}
		}
		return CollisionStatus.instance(actor, map, position, true, PassageBlockerType.PASSABLE);
	}

}
