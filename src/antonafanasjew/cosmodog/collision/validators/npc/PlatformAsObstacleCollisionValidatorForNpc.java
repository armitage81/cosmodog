package antonafanasjew.cosmodog.collision.validators.npc;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerDescriptor;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.PiecesUtils;

/**
 * This validator is checking only the not-occupied platform as obstacle for enemies (They cannot enter it at all) 
 */
public class PlatformAsObstacleCollisionValidatorForNpc extends AbstractCollisionValidator {


	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		
		boolean blocked = false;
		
		Platform platform = cosmodogGame.getMap().getCachedPlatform(cosmodogGame);
		
		if (platform != null) { //It is null, if collected, that is if the player is sitting inside of it. 
		
			if (PiecesUtils.distanceBetweenPieces(actor, platform) <= 10) {
				blocked = CosmodogMapUtils.isTileOnPlatform(tileX, tileY);
			}
		}
		return CollisionStatus.instance(actor, map, tileX, tileY, !blocked, PassageBlockerDescriptor.fromPassageBlockerType(blocked ? PassageBlockerType.BLOCKED : PassageBlockerType.PASSABLE));
		
	}

}
