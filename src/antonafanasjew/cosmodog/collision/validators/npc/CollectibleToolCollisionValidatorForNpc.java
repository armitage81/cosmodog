package antonafanasjew.cosmodog.collision.validators.npc;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;

import java.util.List;

public class CollectibleToolCollisionValidatorForNpc extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		
		CollisionStatus retVal = CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);

		List<Piece> collectibleItems = map.getMapPieces().piecesAtPosition(piece -> piece instanceof CollectibleTool, entrance.getPosition().getX(), entrance.getPosition().getY());

		if (!collectibleItems.isEmpty()) {
		
			retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED);

		}
		return retVal;
		
	}

}
