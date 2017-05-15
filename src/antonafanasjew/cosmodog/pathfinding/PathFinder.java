package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Describes the contract for a path finder. A possible implementation can be the A* algorithm 
 */
public interface PathFinder {

	/**
	 * Returns the movement action result as calculated considering the collision, blocking units and the terrain crossing costs.
	 * 
	 * @param actor The actor for whom the movement action result will be calculated. (Mostly, NPC)
	 * @param costBudget The cost budget that can be used for crossing tiles.
	 * @param collisionValidator Validator to check collision
	 * @param playerMovementActionResult Result of the player movement. Needed to calculate the path related to the result of players movement and not his actual position.
	 * @return The movement action result for the given actor including the path he will take and the costs he will need.
	 */
	MovementActionResult calculateMovementResult(Actor actor, int costBudget, CollisionValidator collisionValidator, MovementActionResult playerMovementActionResult);
	
}
