package antonafanasjew.cosmodog.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;

import antonafanasjew.cosmodog.actions.movement.MovementPlan;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class RandomMovementPlaner extends AbstractMovementPlaner {

	private Supplier<Boolean> randomBooleanSupplier = () -> new Random().nextBoolean();
	private Supplier<Integer> randomIntegerSupplier = () -> new Random().nextInt();

	private Supplier<CosmodogGame> cosmodogGameSupplier = ApplicationContextUtils::getCosmodogGame;
	private Supplier<CosmodogMap> cosmodogMapSupplier = ApplicationContextUtils::mapOfPlayerLocation;

	@Override
	protected MovementPlan calculateMovementPlanInternal(Actor enemy, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance) {
		CosmodogGame game = cosmodogGameSupplier.get();
		CosmodogMap map = cosmodogMapSupplier.get();

		Position targetPosition = null;

		List<Position> possibleTargetPositions = new ArrayList<>();
		possibleTargetPositions.add(enemy.getPosition().nextPosition(DirectionType.DOWN));
		possibleTargetPositions.add(enemy.getPosition().nextPosition(DirectionType.UP));
		possibleTargetPositions.add(enemy.getPosition().nextPosition(DirectionType.LEFT));
		possibleTargetPositions.add(enemy.getPosition().nextPosition(DirectionType.RIGHT));


		
		boolean shouldMove = randomBooleanSupplier.get();
		
		if (shouldMove) {
		
			int firstIndex = randomIntegerSupplier.get();
			if (firstIndex < 0) {
				firstIndex = - firstIndex;
			}
			firstIndex = firstIndex % 4;
			
			
			
			for (int i = 0; i < 4; i++) {

				int index = (firstIndex + i) % 4;
				Position possibleTargetPosition = possibleTargetPositions.get(index);
				Entrance entrance = Entrance.instance(possibleTargetPosition, enemy.getDirection());
				CollisionStatus collisionStatus = collisionValidator.collisionStatus(game, enemy, map, entrance);
				
				if (collisionStatus.isPassable()) {
					targetPosition = possibleTargetPosition;
					break;
				}
			}
		}
		MovementPlan retVal = null;

		if (targetPosition != null) {
			return MovementPlan.instance(enemy.getPosition(), targetPosition);
		} else {
			return MovementPlan.instance(enemy.getPosition());
		}
	}

	public void setRandomBooleanSupplier(Supplier<Boolean> randomBooleanSupplier) {
		this.randomBooleanSupplier = randomBooleanSupplier;
	}

	public void setRandomIntegerSupplier(Supplier<Integer> randomIntegerSupplier) {
		this.randomIntegerSupplier = randomIntegerSupplier;
	}

	public void setCosmodogGameSupplier(Supplier<CosmodogGame> cosmodogGameSupplier) {
		this.cosmodogGameSupplier = cosmodogGameSupplier;
	}

	public void setCosmodogMapSupplier(Supplier<CosmodogMap> cosmodogMapSupplier) {
		this.cosmodogMapSupplier = cosmodogMapSupplier;
	}
}
