package antonafanasjew.cosmodog.collision;

import java.util.Iterator;

import org.newdawn.slick.tiled.TiledMap;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Uses underlying collision validators to validate collision. If at least one is blocking, the result of this validator will block. 
 */
public class OneBlocksAllCollisionValidator implements CollisionValidator {

	private Iterable<CollisionValidator> underlyings;

	public OneBlocksAllCollisionValidator(Iterable<CollisionValidator> underlyings) {
		this.underlyings = underlyings;
	}
	
	@Override
	public CollisionStatus collisionStatus(CosmodogGame cosmodogGame, Actor actor, TiledMap map, int tileX, int tileY) {
		
		
		Iterator<CollisionValidator> it = underlyings.iterator();
		
		while(it.hasNext()) {
			CollisionValidator underlying = it.next();
			CollisionStatus underlyingCollisionStatus = underlying.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			if (underlyingCollisionStatus.isPassable() == false) {
				return underlyingCollisionStatus;
			}
		}
			
		return CollisionStatus.instance(actor, map, tileX, tileY, true, CollisionStatus.NO_PASSAGE_REASON_NO_REASON);
	}

}
