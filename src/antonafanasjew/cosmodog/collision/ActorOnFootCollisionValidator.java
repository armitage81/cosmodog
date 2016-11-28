package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Same as {@link UnequippedWalkerCollisionValidator}.
 */
public class ActorOnFootCollisionValidator extends AbstractCollisionValidator {
	
	private UnequippedWalkerCollisionValidator delegate = new UnequippedWalkerCollisionValidator();
	
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		return delegate.calculateStatusWithinMap(cosmodogGame, actor, map, tileX, tileY);
	}

}
