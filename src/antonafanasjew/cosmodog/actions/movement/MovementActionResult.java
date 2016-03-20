package antonafanasjew.cosmodog.actions.movement;

import java.util.List;

import org.newdawn.slick.util.pathfinding.Path;

import com.google.common.collect.Lists;

/**
 * This class represents the result of a movement action.
 * It will contain precalculated (on action trigger) values which will be applied when the 
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

	public static MovementActionResult instance(Path path, List<Float> movementCostsInPlanetaryMinutes) {
		MovementActionResult retVal = new MovementActionResult();
		retVal.path = path;
		retVal.movementCostsInPlanetaryMinutes = movementCostsInPlanetaryMinutes;
		return retVal;
	}
	
	public static MovementActionResult instance(int startPosX, int startPosY, int targetPosX, int targetPosY, float movementCostsInPlanetaryMinutesForOneStep) {
		Path path = new Path();
		path.appendStep(startPosX, startPosY);
		path.appendStep(targetPosX, targetPosY);
		List<Float> movementCostsInPlanetaryMinutes = Lists.newArrayList(movementCostsInPlanetaryMinutesForOneStep);
		return instance(path, movementCostsInPlanetaryMinutes);
	}
	
	private Path path = new Path();
	
	private List<Float> movementCostsInPlanetaryMinutes;
	
	
	public Path getPath() {
		return path;
	}

	public List<Float> getMovementCostsInPlanetaryMinutes() {
		return movementCostsInPlanetaryMinutes;
	}


	public float getMovementCostsInPlanetaryMinutesForFirstStep() {
		return movementCostsInPlanetaryMinutes.get(0);
	}
	
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
