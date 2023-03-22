package antonafanasjew.cosmodog.collision.validators.player;

import java.util.concurrent.Callable;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * The only purpose of this validator is to switch off collision in case this feature is deactivated.
 * Otherwise it will just pass the validation to the underlying validator.
 */
public class FeatureBoundCollisionValidatorForPlayer extends AbstractCollisionValidator {

	private CollisionValidator delegate;
	
	/**
	 * Initialized with the delegate.
	 * @param delegate The actual collision validator that will be used in case the collision feature is activated.
	 */
	public FeatureBoundCollisionValidatorForPlayer(CollisionValidator delegate) {
		this.delegate = delegate;
	}

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		
		CollisionStatus defaultCollisionStatus = CollisionStatus.instance(actor, map, tileX, tileY, true, PassageBlockerType.PASSABLE);
		
		return Features.getInstance().<CollisionStatus>featureBoundFunction(Features.FEATURE_COLLISION, new Callable<CollisionStatus>() {

			@Override
			public CollisionStatus call() throws Exception {
				return delegate.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			}
			
		}, defaultCollisionStatus);
	}

}
