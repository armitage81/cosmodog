package antonafanasjew.cosmodog.collision.validators.moveable;

import antonafanasjew.cosmodog.topology.Position;
import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.validators.OneBlocksAllCollisionValidator;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

public class GeneralCollisionValidatorForMoveable extends AbstractCollisionValidator {

	private final CollisionValidator collisionValidator;

	public GeneralCollisionValidatorForMoveable() {
		
		CollisionValidator c1 = new InterCharacterCollisionValidatorForMoveable();
		CollisionValidator c2 = new EnergyWallCollisionValidatorForMoveable();
		CollisionValidator c3 = new VehicleAsObstacleCollisionValidatorForMoveable();
		CollisionValidator c4 = new CollectibleCollisionValidatorForMoveable();
		CollisionValidator c5 = new GreenOnlyCollisionValidatorForMoveable();
		CollisionValidator c6 = new DynamicPieceCollisionValidatorForMoveable();
		
		collisionValidator = new OneBlocksAllCollisionValidator(Lists.newArrayList(c1, c2, c3, c4, c5, c6));
	}
	
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		return collisionValidator.collisionStatus(cosmodogGame, actor, map, position);
	}
	
}
