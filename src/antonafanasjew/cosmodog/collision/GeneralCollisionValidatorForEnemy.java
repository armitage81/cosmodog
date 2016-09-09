package antonafanasjew.cosmodog.collision;

import java.util.Map;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;

import com.google.common.collect.Lists;

public class GeneralCollisionValidatorForEnemy extends AbstractCollisionValidator {

	private CollisionValidator collisionValidator;

	private GeneralCollisionValidatorForEnemy(MovementActionResult playerMovementActionResult, Map<Enemy, MovementActionResult> enemyMovementActionResults) {
		CollisionValidator c1 = new ChaussieBasedCollisionValidator();
		CollisionValidator c2 = new InterCharacterCollisionValidator(playerMovementActionResult, enemyMovementActionResults);
		CollisionValidator c3 = new NpcHomeRegionCollisionValidator();
		CollisionValidator c4 = new VehicleObstacleCollisionValidator();
		CollisionValidator c5 = new PlatformAsObstacleForEnemyCollisionValidator();
		CollisionValidator c6 = new PlayerInPlatformAsObstacleCollisionValidator(playerMovementActionResult);
		collisionValidator = new OneBlocksAllCollisionValidator(Lists.newArrayList(c1, c2, c3, c4, c5, c6));
	}
	
	public static GeneralCollisionValidatorForEnemy instance(MovementActionResult playerMovementActionResult, Map<Enemy, MovementActionResult> enemyMovementActionResults) {
		return new GeneralCollisionValidatorForEnemy(playerMovementActionResult, enemyMovementActionResults);
	}
	
	
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		return collisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
	}


}
