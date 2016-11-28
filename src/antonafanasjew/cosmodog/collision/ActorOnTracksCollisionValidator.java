package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Defines collision for actors on tracks (f.i. tanks).
 * Currently, it is the same as {@link UnequippedWalkerCollisionValidator}.  
 */
public class ActorOnTracksCollisionValidator extends AbstractCollisionValidator {

	private UnequippedWalkerCollisionValidator delegate = new UnequippedWalkerCollisionValidator();
	
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		return delegate.calculateStatusWithinMap(cosmodogGame, actor, map, tileX, tileY);
	}

}
