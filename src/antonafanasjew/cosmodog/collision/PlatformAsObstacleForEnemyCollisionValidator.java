package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.PiecesUtils;

/**
 * This validator is checking only the not-occupied platform as obstacle for enemies (They cannot enter it at all) 
 */
public class PlatformAsObstacleForEnemyCollisionValidator extends AbstractCollisionValidator {


	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		
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
