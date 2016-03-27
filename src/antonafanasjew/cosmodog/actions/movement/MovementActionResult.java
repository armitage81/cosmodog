package antonafanasjew.cosmodog.actions.movement;

import java.util.List;

import org.newdawn.slick.util.pathfinding.Path;

import com.google.common.collect.Lists;

/**
 * This class represents the result of a movement action.
 * It will contain pre-calculated (on action trigger) values which will be applied when the 
 * action will finish.
 * 
 * The reason for this is because we need to know the result of the action while the action is
 * executed. F.i. An enemy needs to know where the player will be at the end of the action
 * to not bump into him.
 * 
 * The movement can consist of more than one step (f.i. for enemies that move with double speed.)
 * So the movement is represented as a path with the start position as the first step.
 * 
 */
public class MovementActionResult {

	/**
	 * Returns the movement action result instance based on a path and movement costs.
	 * @param path Path of the movement.
	 * @param movementCostsInPlanetaryMinutes Costs in planetary minutes for each step in the path.
	 * @return Movement action result.
	 */
	public static MovementActionResult instance(Path path, List<Float> movementCostsInPlanetaryMinutes) {
		MovementActionResult retVal = new MovementActionResult();
		retVal.path = path;
		retVal.movementCostsInPlanetaryMinutes = movementCostsInPlanetaryMinutes;
		return retVal;
	}

	/**
	 * Returns the movement action result instance based on the start and the target positions and the movement action costs.
	 * @param startPosX x position before the movement.
	 * @param startPosY y position before the movement.
	 * @param targetPosX Target x position.
	 * @param targetPosY Target y position.
	 * @param movementCostsInPlanetaryMinutesForOneStep Movement costs in planetary minutes.
	 * @return Movement action result.
	 */
	public static MovementActionResult instance(int startPosX, int startPosY, int targetPosX, int targetPosY, float movementCostsInPlanetaryMinutesForOneStep) {
		Path path = new Path();
		path.appendStep(startPosX, startPosY);
		path.appendStep(targetPosX, targetPosY);
		List<Float> movementCostsInPlanetaryMinutes = Lists.newArrayList(movementCostsInPlanetaryMinutesForOneStep);
		return instance(path, movementCostsInPlanetaryMinutes);
	}
	
	private Path path = new Path();
	
	private List<Float> movementCostsInPlanetaryMinutes;
	
	/**
	 * Returns the path of the movement. First location is the start position.
	 * @return Path of the movement.
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * Costs for the movement in planetary minutes. Each path step has its own costs.
	 * @return Costs for each step in the path.
	 */
	public List<Float> getMovementCostsInPlanetaryMinutes() {
		return movementCostsInPlanetaryMinutes;
	}

	/**
	 * Costs for the first movement step in the path in planetary minutes.
	 * @return Costs for the first step in the path.
	 */
	public float getMovementCostsInPlanetaryMinutesForFirstStep() {
		return movementCostsInPlanetaryMinutes.get(0);
	}
	
	/**
	 * Returns the planetary minutes that remain after spending the given planetary minutes
	 * on path steps. (If steps cost 3,4,5,4 minutes and the parameter is 15, the result will be 3 as the costs for the last step (4) are too high)
	 * @param passedPlanetaryMinutes Minutes to apply on the path.
	 * @return Remaining minutes before the last not taken step, or -1 if the whole path can be executed by the given time.
	 */
	public float getRemainingPlanetaryMinutesSinceLastMovementStep(float passedPlanetaryMinutes) {
		int accumulatedMinutes = 0;
		
		for (int i = 0; i < movementCostsInPlanetaryMinutes.size(); i++) {
			accumulatedMinutes += movementCostsInPlanetaryMinutes.get(i);
			
			if (accumulatedMinutes > passedPlanetaryMinutes) {
				accumulatedMinutes -= movementCostsInPlanetaryMinutes.get(i);
				return passedPlanetaryMinutes - accumulatedMinutes;
			}
		}
		
		return -1;
	}
	
	/**
	 * Returns the index of the step in the path that will be executed after the giben minutes.
	 * @param passedPlanetaryMinutes Planetary minutes.
	 * @return The step in the path that will be executed at this time.
	 */
	public int getMovementStepIndexForPassedPlanetaryMinutes(float passedPlanetaryMinutes) {
		
		int accumulatedMinutes = 0;
		
		for (int i = 0; i < movementCostsInPlanetaryMinutes.size(); i++) {
			accumulatedMinutes += movementCostsInPlanetaryMinutes.get(i);
			
			if (accumulatedMinutes > passedPlanetaryMinutes) {
				return i;
			}
		}
		
		return -1;
		
	}
	
}
