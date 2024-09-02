package antonafanasjew.cosmodog.actions.movement;

import java.util.List;

import org.newdawn.slick.util.pathfinding.Path;

import com.google.common.collect.Lists;

/**
 * Represents the result of a movement action which is executed when the player moves or skips turn.
 * <p>
 * This result contains the precalculated values for an actor that is involved in the movement
 * (player, moveable actor, enemy).
 * <p>
 * Take note: This result relates to ONE actor only.
 * Each actor that is involved in the movement will have its own result (see MovementAction::onTrigger).
 * <p>
 * It will contain precalculated (on action trigger) target position which will be applied when the
 * action will finish.
 * <p>
 * This precalculated target position is to know the result of the action while the action is
 * executed. F.i. An enemy needs to know where the player will be at the end of the action
 * to not bump into him.
 * <p>
 * Example: When the player moves, he blocks his target position. When there are multiple enemies around, they
 * will also move and block their target positions. So when calculating the movement path of an enemy, the player's
 * target position and the target positions of the other enemies must be considered.
 * Same is valid for moveable objects. All of these positions are held
 * in the movement action results of the corresponding actors.
 * <p>
 * The movement can consist of more than one step (f.i. for enemies that move with double speed.)
 * So the movement is represented as a path with the start position as the first step.
 * <p>
 * Take note: If an actor does not move, the path will still contain two elements,
 * the start position and the target position that are equal. (This is valid only for the player. Enemies will have only the start
 * position in their path in this case.) If an actor moves
 * the path will contain the start position and all passed positions up to the target position. For the player's
 * movement, the path will contain the start position and the target position of an adjacent tile.
 * <p>
 * Take note: When creating a result object, the whole path of the movement must be known already. The movement
 * action result object is only to hold the values of this path.
 */
public class MovementActionResult {

	/**
	 * Returns the movement action result instance based on a path.
	 *
	 * @param path Path of the movement. First element is the start position of the actor.
	 * @return Movement action result.
	 */
	public static MovementActionResult instance(Path path) {
		MovementActionResult retVal = new MovementActionResult();
		retVal.path = path;
		return retVal;
	}

	/**
	 * Returns the movement action result instance based on the start and the target positions.
	 * <p>
	 * This is the convenience method for creating a movement action result for a single step movement (mostly, for the player).
	 * <p>
	 * Take note: The start position and the target position can be equal if the player does not move (skip turn).
	 *
	 * @param startPosX x position of the actor before the movement.
	 * @param startPosY y position of the actor before the movement.
	 * @param targetPosX Target x position of the actor.
	 * @param targetPosY Target y position of the actor.
	 * @return Movement action result.
	 */
	public static MovementActionResult instance(int startPosX, int startPosY, int targetPosX, int targetPosY) {
		Path path = new Path();
		path.appendStep(startPosX, startPosY);
		path.appendStep(targetPosX, targetPosY);
		return instance(path);
	}

	/**
	 * Contains the path of the movement. First location is the start position.
	 * Subsequent locations are the steps of the movement. A "skip turn" action will cause a path with two elements
	 * that are equal (only valid for the player).
	 */
	private Path path = new Path();
	
	/**
	 * Returns the path of the movement. First location is the start position.
	 * Subsequent locations are the steps of the movement. A "skip turn" action will cause a path with two elements
	 * that are equal (only valid for the player).
	 *
	 * @return Path of the movement.
	 */
	public Path getPath() {
		return path;
	}
	
}
