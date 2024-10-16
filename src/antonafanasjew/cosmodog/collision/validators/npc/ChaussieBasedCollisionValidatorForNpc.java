package antonafanasjew.cosmodog.collision.validators.npc;

import java.util.Map;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.domains.ChaussieType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.NpcActor;

import antonafanasjew.cosmodog.topology.Position;
import com.google.common.collect.Maps;

/**
 * Calculates collision based on the chaussie of an NPC. Use it only for NPC's
 *
 */
public class ChaussieBasedCollisionValidatorForNpc extends AbstractCollisionValidator {

	private Map<ChaussieType, CollisionValidator> chaussieCollisionValidators = Maps.newHashMap();
	
	public ChaussieBasedCollisionValidatorForNpc() {
		chaussieCollisionValidators.put(ChaussieType.FEET, new WalkingCollisionValidatorForNpc());
		chaussieCollisionValidators.put(ChaussieType.FLYING, new FlyingCollisionValidatorForNpc());
		chaussieCollisionValidators.put(ChaussieType.HOVERING, new HoveringCollisionValidatorForNpc());
		chaussieCollisionValidators.put(ChaussieType.SWIMMING, new SailingCollisionValidatorForNpc());
		chaussieCollisionValidators.put(ChaussieType.TRACKS, new RollingCollisionValidatorForNpc());
		chaussieCollisionValidators.put(ChaussieType.WHEELS, new DrivingCollisionValidatorForNpc());
	}
	
	private CollisionValidator defaulCollisionValidator = new WalkingCollisionValidatorForNpc();

	
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		
		NpcActor npcActor = (NpcActor)actor;
		
		CollisionValidator collisionValidator = chaussieCollisionValidators.get(npcActor.getChaussieType());
		
		if (collisionValidator == null) {
			collisionValidator = defaulCollisionValidator;
		}
		
		return collisionValidator.collisionStatus(cosmodogGame, npcActor, map, position);
	}

}
