package antonafanasjew.cosmodog.collision.validators.player;

import java.util.Set;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.caching.PiecePredicates;
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
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.portals.Entrance;
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
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {

		CosmodogMap cosmodogMap = cosmodogGame.mapOfPlayerLocation();
		
		Set<Enemy> enemies = cosmodogMap.allEnemies();
		//Check blocking enemies
		for (Enemy enemy : enemies) {
			if (PiecesUtils.distanceBetweenPieces(enemy, actor) <= 10) {
				if (CosmodogMapUtils.isTileOnPlatform(enemy.getPosition(), entrance.getPosition())) {
					return CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED);
				}
			}
		}
		//Check blocking vehicles and collectibles. The platform is blocked only if the piece is not on the platform, but would be after platform movement.
		for (Piece piece : cosmodogMap.getMapPieces().piecesOverall(PiecePredicates.ALWAYS_TRUE)) {
			if (piece instanceof Vehicle || piece instanceof Collectible) {
				if (PiecesUtils.distanceBetweenPieces(piece, actor) <= 10) {
					if (CosmodogMapUtils.isTileOnPlatform(piece.getPosition(), entrance.getPosition())) {
						if (!CosmodogMapUtils.isTileOnPlatform(piece.getPosition(), actor.getPosition())) {
							return CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED);
						}
					}
				}
			}
		}

		//Check if another platform is in the way
		Set<Platform> otherPlatforms = cosmodogMap.getPlatforms().stream().filter(p -> !p.getPosition().equals(actor.getPosition())).collect(Collectors.toSet());
		for (Platform otherPlatform : otherPlatforms) {
			int targetX = (int)entrance.getPosition().getX();
			int targetY = (int)entrance.getPosition().getY();
			int otherPlatformX = (int)otherPlatform.getPosition().getX();
			int otherPlatformY = (int)otherPlatform.getPosition().getY();
			int diff_x = targetX - otherPlatformX;
			int diff_y = targetY - otherPlatformY;

			//This numbers are validated with checking all possible platform overlappings. (Did it with excel by laying over platform shapes.)
			if (
				Math.abs(diff_x) <= 5 && Math.abs(diff_y) <= 5
						||
				Math.abs(diff_x) <= 7 && Math.abs(diff_y) <= 3
						||
				diff_x == 6 && diff_y == 4
						||
				diff_x == 4 && diff_y == 6
						||
				diff_x == 3 && diff_y == 6
						||
				diff_x == 2 && diff_y == 6
						||
				diff_x == 0 && diff_y == 6
						||
				diff_x == -1 && diff_y == 6
						||
				diff_x == -2 && diff_y == 6
						||
				diff_x == -4 && diff_y == 6
						||
				diff_x == -6 && diff_y == 4
						||
				diff_x == -8 && diff_y == 0
						||
				diff_x == -6 && diff_y == -4
						||
				diff_x == -4 && diff_y == -6
						||
				diff_x == -3 && diff_y == -6
						||
				diff_x == -2 && diff_y == -6
						||
				diff_x == 0 && diff_y == -6
						||
				diff_x == 1 && diff_y == -6
						||
				diff_x == 2 && diff_y == -6
						||
				diff_x == 4 && diff_y == -6
						||
				diff_x == 6 && diff_y == -4
						||
				diff_x == 8 && diff_y == -0
			){
				return CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED);
			}

		}

		//Check missing rails
		int tileId = map.getTileId(entrance.getPosition(), Layers.LAYER_META_PLATFORM_COLLISION);
		boolean passable = TileType.FREE_PLATFORM_PASSAGE.getTileId() == tileId;

		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED_NO_RAILS;
		return CollisionStatus.instance(actor, map, entrance, passable, passageBlocker);
		
	}

}
