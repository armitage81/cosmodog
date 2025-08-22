package antonafanasjew.cosmodog.actions.movement;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.util.pathfinding.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovementPlan {

	private Position startPosition;
	private final List<Position> movementSteps = new ArrayList<>();


	public static MovementPlan instance(Position startPosition, Position targetPosition) {
		List<Position> movementSteps = new ArrayList<>();
		movementSteps.add(targetPosition);
		return instance(startPosition, movementSteps);
	}

	public static MovementPlan instance(Position startPosition) {
		return instance(startPosition, new ArrayList<>());
	}

	public static MovementPlan instance(Position startPosition, List<Position> movementSteps) {
		MovementPlan retVal = new MovementPlan();
		retVal.startPosition = startPosition;
		retVal.movementSteps.addAll(movementSteps);
		return retVal;
	}

	public Position getStartPosition() {
		return startPosition;
	}

	public List<Position> getMovementSteps() {
		return movementSteps;
	}

	public Position positionAfterExecution() {

		if (movementSteps.isEmpty()) {
			return startPosition;
		} else {
			return movementSteps.getLast();
		}

	}

	public boolean movementPlanned() {
		return !movementSteps.isEmpty();
	}

	public int numberOfStepsPlanned() {
		return movementSteps.size();
	}
}
