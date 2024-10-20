package antonafanasjew.cosmodog.collision.validators.npc;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.MoveableDynamicPiece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.dynamicpieces.AlienBaseBlockade;
import antonafanasjew.cosmodog.model.dynamicpieces.Bamboo;
import antonafanasjew.cosmodog.model.dynamicpieces.BinaryIndicator;
import antonafanasjew.cosmodog.model.dynamicpieces.Crate;
import antonafanasjew.cosmodog.model.dynamicpieces.CrumbledWall;
import antonafanasjew.cosmodog.model.dynamicpieces.Door;
import antonafanasjew.cosmodog.model.dynamicpieces.Gate;
import antonafanasjew.cosmodog.model.dynamicpieces.HardStone;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.dynamicpieces.Poison;
import antonafanasjew.cosmodog.model.dynamicpieces.PressureButton;
import antonafanasjew.cosmodog.model.dynamicpieces.SecretDoor;
import antonafanasjew.cosmodog.model.dynamicpieces.Stone;
import antonafanasjew.cosmodog.model.dynamicpieces.Terminal;
import antonafanasjew.cosmodog.model.dynamicpieces.Tree;
import antonafanasjew.cosmodog.topology.Position;

public class DynamicPieceCollisionValidatorForNpc extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, position, true, PassageBlockerType.PASSABLE);
		DynamicPiece dynamicPiece = cosmodogGame.dynamicPieceAtPosition(position);
		if (dynamicPiece != null) {
			if (dynamicPiece instanceof Mine) {
				//Do nothing. Just a place holder to not forget this part in case something changes.
			}
			if (dynamicPiece instanceof Poison) {
				//Do nothing. Just a place holder to not forget this part in case something changes.
			}
			if (dynamicPiece instanceof PressureButton) {
				//Do nothing. Just a place holder to not forget this part in case something changes.
			}	
			
			if (dynamicPiece instanceof MoveableDynamicPiece) {
				retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
			} else if (dynamicPiece instanceof Terminal) {
				retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
			} else if (dynamicPiece instanceof BinaryIndicator) {
				retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
			} else if (dynamicPiece instanceof Stone stone) {
                if (stone.getState() != Stone.STATE_DESTROYED) {
					retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof HardStone hardStone) {
                if (hardStone.getState() != Stone.STATE_DESTROYED) {
					retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof Tree tree) {
                if (tree.getState() != Tree.STATE_DESTROYED) {
					retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof Bamboo bamboo) {
                if (bamboo.getState() != Bamboo.STATE_DESTROYED) {
					retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof CrumbledWall wall) {
                if (wall.getState() != CrumbledWall.STATE_DESTROYED) {
					retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof Gate gate) {
                if (!gate.isLowered()) {
					retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof SecretDoor door) {
                if (!door.isOpen()) {
					retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof Crate crate) {
                if (crate.getState() != Crate.STATE_DESTROYED) {
					retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof Door door) {
                if (door.closed()) {
					retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				} 
			} else if (dynamicPiece instanceof AlienBaseBlockade alienBaseBlockade) {
                if (alienBaseBlockade.closed()) {
					retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			}
		}
		return retVal;
	}

}
