package antonafanasjew.cosmodog.collision.validators.player;

import java.util.Set;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.PiecesUtils;

/**
 * This validator is for the occupied platform itself. It checks whether the platform can move. 
 */
public class PlatformAsVehicleCollisionValidatorForPlayer extends AbstractCollisionValidator {

	/**
	 * This collision validator is for the platform on rails. It checks a separate collision layer.
	 */
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {

		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogMap();
		
		Set<Enemy> enemies = cosmodogMap.getEnemies();
		//Check blocking enemies
		for (Enemy enemy : enemies) {
			if (PiecesUtils.distanceBetweenPieces(enemy, actor) <= 10) {
				if (CosmodogMapUtils.isTileOnPlatform(enemy.getPosition(), position)) {
					return CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED);
				}
			}
		}
		//Check blocking vehicles and collectibles. The platform is blocked only if the piece is not on the platform, but would be after platform movement.
		for (Piece piece : cosmodogMap.getMapPieces().values()) {
			if (piece instanceof Vehicle || piece instanceof Collectible) {
				if (PiecesUtils.distanceBetweenPieces(piece, actor) <= 10) {
					if (CosmodogMapUtils.isTileOnPlatform(piece.getPosition(), position)) {
						if (!CosmodogMapUtils.isTileOnPlatform(piece.getPosition(), actor.getPosition())) {
							return CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED);
						}
					}
				}
			}
		}

		//Check missing rails
		int tileId = map.getTileId(position, Layers.LAYER_META_PLATFORM_COLLISION);
		boolean passable = TileType.FREE_PLATFORM_PASSAGE.getTileId() == tileId;

		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED_NO_RAILS;
		return CollisionStatus.instance(actor, map, position, passable, passageBlocker);
		
	}

}
