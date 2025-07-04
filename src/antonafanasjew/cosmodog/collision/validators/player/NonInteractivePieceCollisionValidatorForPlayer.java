package antonafanasjew.cosmodog.collision.validators.player;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Blocks passage by car over inactive collectibles.
 * 
 * Reason is a workaround to avoid two pieces to be on the same tile.
 * It could be handled by a composite collectible (like in case of enemy drops) 
 * but the vehicle is not a collectible.
 * 
 */
public class NonInteractivePieceCollisionValidatorForPlayer extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		Piece piece = map.pieceAtTile(entrance.getPosition());
		boolean passable = piece == null || piece.interactive(piece, ApplicationContext.instance(), cosmodogGame, (Player)actor);

		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED_BY_INACTIVE_PIECE;
		return CollisionStatus.instance(actor, map, entrance, passable, passageBlocker);
	}

}
