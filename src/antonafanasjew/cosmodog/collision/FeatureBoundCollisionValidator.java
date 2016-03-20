package antonafanasjew.cosmodog.collision;

import java.util.concurrent.Callable;

import org.newdawn.slick.tiled.TiledMap;

import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * The only purpose of this validator is to switch off collision in case this feature is deactivated.
 * Otherwise it will just pass the validation to the underlying validator.
 */
public class FeatureBoundCollisionValidator extends AbstractCollisionValidator {

	private CollisionValidator delegate;
	
	public FeatureBoundCollisionValidator(CollisionValidator delegate) {
		this.delegate = delegate;
	}

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, TiledMap map, int tileX, int tileY) {
		
		CollisionStatus defaultCollisionStatus = CollisionStatus.instance(actor, map, tileX, tileY, true, CollisionStatus.NO_PASSAGE_REASON_NO_REASON);
		
		return Features.getInstance().<CollisionStatus>featureBoundFunction(Features.FEATURE_COLLISION, new Callable<CollisionStatus>() {

			@Override
			public CollisionStatus call() throws Exception {
				return delegate.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			}
			
		}, defaultCollisionStatus);
	}

}
