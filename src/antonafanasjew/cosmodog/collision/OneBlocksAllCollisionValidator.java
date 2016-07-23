package antonafanasjew.cosmodog.collision;

import java.util.Iterator;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Uses underlying collision validators to validate collision. If at least one is blocking, the result of this validator will block. 
 */
public class OneBlocksAllCollisionValidator implements CollisionValidator {

	private Iterable<CollisionValidator> underlyings;

	/**
	 * Initialized with underlying validators.
	 * @param underlyings Underlying validators.
	 */
	public OneBlocksAllCollisionValidator(Iterable<CollisionValidator> underlyings) {
		this.underlyings = underlyings;
	}
	
	@Override
	public CollisionStatus collisionStatus(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		
		
		Iterator<CollisionValidator> it = underlyings.iterator();
		
		while(it.hasNext()) {
			CollisionValidator underlying = it.next();
			CollisionStatus underlyingCollisionStatus = underlying.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			if (underlyingCollisionStatus.isPassable() == false) {
				return underlyingCollisionStatus;
			}
		}
			
		return CollisionStatus.instance(actor, map, tileX, tileY, true, PassageBlockerType.PASSABLE);
	}

}
