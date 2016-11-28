package antonafanasjew.cosmodog.collision;

import java.util.Set;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.PiecesUtils;

/**
 * This validator is for the occupied platform itself. It checks whether the platform can move. 
 */
public class PlatformAsVehicleCollisionValidator extends AbstractCollisionValidator {

	/**
	 * This collision validator is for the platform on rails. It checks a separate collision layer.
	 */
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {

		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogMap();
		
		Set<Enemy> enemies = cosmodogMap.getEnemies();
		//Check blocking enemies
		for (Enemy enemy : enemies) {
			if (PiecesUtils.distanceBetweenPieces(enemy, actor) <= 10) {
				if (CosmodogMapUtils.isTileOnPlatform(enemy.getPositionX(), enemy.getPositionY(), tileX, tileY)) {
					return CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.BLOCKED);			
				}
			}
		}
		//Check blocking vehicles. The platform is blocked only if the vehicle is not on the platform, but would be after platform movement.
		for (Piece piece : cosmodogMap.getMapPieces()) {
			if (piece instanceof Vehicle) {
				if (PiecesUtils.distanceBetweenPieces(piece, actor) <= 10) {
					if (CosmodogMapUtils.isTileOnPlatform(piece.getPositionX(), piece.getPositionY(), tileX, tileY)) {
						if (!CosmodogMapUtils.isTileOnPlatform(piece.getPositionX(), piece.getPositionY(), actor.getPositionX(), actor.getPositionY())) {
							return CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.BLOCKED);			
						}
					}
				}
			}
		}

		//Check missing rails
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_PLATFORM_COLLISION);
		boolean passable = TileType.FREE_PLATFORM_PASSAGE.getTileId() == tileId;

		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED_NO_RAILS;
		return CollisionStatus.instance(actor, map, tileX, tileY, passable, passageBlocker);
		
	}

}
