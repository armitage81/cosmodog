package antonafanasjew.cosmodog.collision.validators.npc;

import java.util.Map;

import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;
import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.validators.OneBlocksAllCollisionValidator;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;

public class GeneralCollisionValidatorForNpc extends AbstractCollisionValidator {

	private CollisionValidator collisionValidator;

	private GeneralCollisionValidatorForNpc(Entrance targetEntrance, Entrance moveableTargetEntrance, Map<Enemy, MovementActionResult> enemyMovementActionResults) {
		CollisionValidator c1 = new ChaussieBasedCollisionValidatorForNpc();
		CollisionValidator c2 = new InterCharacterCollisionValidatorForNpc(targetEntrance, enemyMovementActionResults);
		CollisionValidator c3 = new HomeRegionCollisionValidatorForNpc();
		CollisionValidator c4 = new VehicleAsObstacleCollisionValidatorForNpc();
		CollisionValidator c5 = new PlatformAsObstacleCollisionValidatorForNpc();
		CollisionValidator c6 = new PlayerInPlatformAsObstacleCollisionValidatorForNpc(targetEntrance);
		CollisionValidator c7 = new DynamicPieceCollisionValidatorForNpc();
		CollisionValidator c8 = new MoveableTargetCollisionValidatorForNpc(moveableTargetEntrance);
		CollisionValidator c9 = new EnergyWallCollisionValidatorForNpc();
		collisionValidator = new OneBlocksAllCollisionValidator(Lists.newArrayList(c1, c2, c3, c4, c5, c6, c7, c8, c9));
	}
	
	public static GeneralCollisionValidatorForNpc instance(Entrance targetEntrance, Entrance moveableTargetEntrance, Map<Enemy, MovementActionResult> enemyMovementActionResults) {
		return new GeneralCollisionValidatorForNpc(targetEntrance, moveableTargetEntrance, enemyMovementActionResults);
	}
	
	
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		return collisionValidator.collisionStatus(cosmodogGame, actor, map, entrance);
	}


}
