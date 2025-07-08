package antonafanasjew.cosmodog.collision.validators.npc;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerDescriptor;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.PiecesUtils;

import java.util.Set;

/**
 * This validator is checking only the not-occupied platform as obstacle for enemies (They cannot enter it at all) 
 */
public class PlatformAsObstacleCollisionValidatorForNpc extends AbstractCollisionValidator {


	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		
		boolean blocked = false;
		
		Set<Platform> platforms = cosmodogGame.mapOfPlayerLocation().getPlatforms();
		
		for (Platform platform : platforms) {
		
			if (PiecesUtils.distanceBetweenPieces(actor, platform) <= 10) {
				blocked = CosmodogMapUtils.isTileOnPlatform(entrance.getPosition());
			}
		}
		return CollisionStatus.instance(actor, map, entrance, !blocked, PassageBlockerDescriptor.fromPassageBlockerType(blocked ? PassageBlockerType.BLOCKED : PassageBlockerType.PASSABLE));
		
	}

}
