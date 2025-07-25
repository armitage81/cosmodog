package antonafanasjew.cosmodog.collision.validators;

import java.util.Iterator;

import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;

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
	public CollisionStatus collisionStatus(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {


        for (CollisionValidator underlying : underlyings) {
            CollisionStatus underlyingCollisionStatus = underlying.collisionStatus(cosmodogGame, actor, map, entrance);
            if (!underlyingCollisionStatus.isPassable()) {
                return underlyingCollisionStatus;
            }
        }
			
		return CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);
	}

}
