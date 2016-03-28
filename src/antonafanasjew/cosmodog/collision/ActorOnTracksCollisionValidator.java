package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Defines collision for actors on tracks (f.i. tanks).
 * Currently, it is the same as {@link UnequippedWalkerCollisionValidator}.  
 */
public class ActorOnTracksCollisionValidator extends AbstractCollisionValidator {

	private UnequippedWalkerCollisionValidator delegate = new UnequippedWalkerCollisionValidator();
	
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		return delegate.calculateStatusWithinMap(cosmodogGame, actor, map, tileX, tileY);
	}

}
