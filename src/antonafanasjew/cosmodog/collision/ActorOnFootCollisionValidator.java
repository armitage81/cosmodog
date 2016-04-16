package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Same as {@link UnequippedWalkerCollisionValidator}.
 */
public class ActorOnFootCollisionValidator extends AbstractCollisionValidator {
	
	private UnequippedWalkerCollisionValidator delegate = new UnequippedWalkerCollisionValidator();
	
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		return delegate.calculateStatusWithinMap(cosmodogGame, actor, map, tileX, tileY);
	}

}