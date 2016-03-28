package antonafanasjew.cosmodog.collision;

import java.util.Map;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.domains.ChaussieType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.NpcActor;

import com.google.common.collect.Maps;

/**
 * Calculates collision based on the chaussie of an NPC. Use it only for NPC's
 *
 */
public class ChaussieBasedCollisionValidator extends AbstractCollisionValidator {

	private Map<ChaussieType, CollisionValidator> chaussieCollisionValidators = Maps.newHashMap();
	
	public ChaussieBasedCollisionValidator() {
		chaussieCollisionValidators.put(ChaussieType.FEET, new ActorOnFootCollisionValidator());
		chaussieCollisionValidators.put(ChaussieType.FLYING, new ActorOnWingsCollisionValidator());
		chaussieCollisionValidators.put(ChaussieType.SWIMMING, new ActorOnBoatCollisionValidator());
		chaussieCollisionValidators.put(ChaussieType.TRACKS, new ActorOnTracksCollisionValidator());
		chaussieCollisionValidators.put(ChaussieType.WHEELS, new ActorOnWheelsCollisionValidator());
	}
	
	private CollisionValidator defaulCollisionValidator = new ActorOnFootCollisionValidator();

	
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		
		NpcActor npcActor = (NpcActor)actor;
		
		CollisionValidator collisionValidator = chaussieCollisionValidators.get(npcActor.getChaussieType());
		
		if (collisionValidator == null) {
			collisionValidator = defaulCollisionValidator;
		}
		
		return collisionValidator.collisionStatus(cosmodogGame, npcActor, map, tileX, tileY);
	}

}
