package antonafanasjew.cosmodog.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;

import antonafanasjew.cosmodog.actions.movement.MovementPlan;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import com.google.common.collect.Maps;

public class RoamingMovementPlaner extends AbstractMovementPlaner {

	private static final float CHANCE_TO_MOVE_IN_THE_SAME_DIRECTION = 0.8f;

	private static final Map<DirectionType, Integer> DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY = Maps.newHashMap();

	static {
		DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY.put(DirectionType.DOWN, 0);
		DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY.put(DirectionType.UP, 1);
		DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY.put(DirectionType.LEFT, 2);
		DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY.put(DirectionType.RIGHT, 3);
	}

	private Supplier<Boolean> randomBooleanSupplier = () -> new Random().nextBoolean();
	private Supplier<Integer> randomIntegerSupplier = () -> new Random().nextInt();
	private Supplier<Integer> randomIntegerSupplier2 = () -> new Random().nextInt();

	private Supplier<CosmodogGame> cosmodogGameSupplier = ApplicationContextUtils::getCosmodogGame;
	private Supplier<CosmodogMap> cosmodogMapSupplier = ApplicationContextUtils::mapOfPlayerLocation;


	@Override
	protected MovementPlan calculateMovementPlanInternal(Actor enemy, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance) {

		Position targetPosition = null;

		CosmodogGame game = cosmodogGameSupplier.get();
		CosmodogMap map = cosmodogMapSupplier.get();

		List<Position> possibleTargetPositions = new ArrayList<>();
		possibleTargetPositions.add(enemy.getPosition().nextPosition(DirectionType.DOWN));
		possibleTargetPositions.add(enemy.getPosition().nextPosition(DirectionType.UP));
		possibleTargetPositions.add(enemy.getPosition().nextPosition(DirectionType.LEFT));
		possibleTargetPositions.add(enemy.getPosition().nextPosition(DirectionType.RIGHT));

		int firstIndex;

		//50% chance to move, 50% chance to stay still.
		boolean shouldMove = randomBooleanSupplier.get();
		
		if (shouldMove) {

			int randomPercentage = randomIntegerSupplier.get();
			if (randomPercentage < 0) {
				randomPercentage = - randomPercentage;
			}
			randomPercentage = randomPercentage % 100;
			boolean continueMovingInSameDirection = randomPercentage <= CHANCE_TO_MOVE_IN_THE_SAME_DIRECTION * 100;

			if (continueMovingInSameDirection) {
				firstIndex = DIRECTION_TYPE_TO_INDEX_IN_TARGET_TILE_ARRAY.get(enemy.getDirection());
			} else {
				firstIndex = randomIntegerSupplier2.get();
				if (firstIndex < 0) {
					firstIndex = - firstIndex;
				}
				firstIndex = firstIndex % 4;
			}

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

	public void setRandomIntegerSupplier2(Supplier<Integer> randomIntegerSupplier2) {
		this.randomIntegerSupplier2 = randomIntegerSupplier2;
	}

	public void setCosmodogGameSupplier(Supplier<CosmodogGame> cosmodogGameSupplier) {
		this.cosmodogGameSupplier = cosmodogGameSupplier;
	}

	public void setCosmodogMapSupplier(Supplier<CosmodogMap> cosmodogMapSupplier) {
		this.cosmodogMapSupplier = cosmodogMapSupplier;
	}
}
