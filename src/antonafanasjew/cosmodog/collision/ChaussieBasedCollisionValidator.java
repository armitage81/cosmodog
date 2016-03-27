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
		chaussieCollisionValidators.put(ChaussieType.FEET, new NpcOnFootCollisionValidator());
		chaussieCollisionValidators.put(ChaussieType.FLYING, new NpcOnWingsCollisionValidator());
		chaussieCollisionValidators.put(ChaussieType.SWIMMING, new NpcOnBoatCollisionValidator());
		chaussieCollisionValidators.put(ChaussieType.TRACKS, new NpcOnTracksCollisionValidator());
		chaussieCollisionValidators.put(ChaussieType.WHEELS, new NpcOnWheelsCollisionValidator());
	}
	
	private CollisionValidator defaulCollisionValidator = new NpcOnFootCollisionValidator();

	
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
