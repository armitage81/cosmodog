package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.Bamboo;
import antonafanasjew.cosmodog.model.dynamicpieces.Crate;
import antonafanasjew.cosmodog.model.dynamicpieces.CrumbledWall;
import antonafanasjew.cosmodog.model.dynamicpieces.HardStone;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.dynamicpieces.Poison;
import antonafanasjew.cosmodog.model.dynamicpieces.PressureButton;
import antonafanasjew.cosmodog.model.dynamicpieces.Stone;
import antonafanasjew.cosmodog.model.dynamicpieces.Tree;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;

public class DynamicPieceCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, tileX, tileY, true, PassageBlockerType.PASSABLE);
		DynamicPiece dynamicPiece = map.dynamicPieceAtPosition(tileX, tileY);
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
			if (dynamicPiece instanceof Stone) {
				Stone stone = (Stone)dynamicPiece;
				if (stone.getState() != Stone.STATE_DESTROYED) {
					retVal = CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof HardStone) {
				HardStone hardStone = (HardStone)dynamicPiece;
				if (hardStone.getState() != Stone.STATE_DESTROYED) {
					
					String blockReasonParam = "<knock>";
					
					if (actor instanceof Player) {
						Player player = (Player)actor;
						Inventory inventory = player.getInventory();
						if (inventory.get(InventoryItemType.PICK) == null) {
							blockReasonParam = "Pick required";
						}
					}
					
					retVal = CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof Tree) {
				Tree tree = (Tree)dynamicPiece;
				if (tree.getState() != Tree.STATE_DESTROYED) {
					
					String blockReasonParam = "<knock>";
					
					if (actor instanceof Player) {
						Player player = (Player)actor;
						Inventory inventory = player.getInventory();
						if (inventory.get(InventoryItemType.AXE) == null) {
							blockReasonParam = "Axe required";
						}
					}
					
					retVal = CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof Bamboo) {
				Bamboo bamboo = (Bamboo)dynamicPiece;
				if (bamboo.getState() != Bamboo.STATE_DESTROYED) {
					
					String blockReasonParam = "<knock>";
					
					if (actor instanceof Player) {
						Player player = (Player)actor;
						Inventory inventory = player.getInventory();
						if (inventory.get(InventoryItemType.MACHETE) == null) {
							blockReasonParam = "Machete required";
						}
					}
					
					retVal = CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof CrumbledWall) {
				CrumbledWall wall = (CrumbledWall)dynamicPiece;
				if (wall.getState() != CrumbledWall.STATE_DESTROYED) {
					
					String blockReasonParam = "";
					
					if (actor instanceof Player) {
						Player player = (Player)actor;
						Inventory inventory = player.getInventory();
						if (inventory.get(InventoryItemType.DYNAMITE) == null) {
							blockReasonParam = "Dynamite required";
						}
					}
					
					retVal = CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof Crate) {
				Crate crate = (Crate)dynamicPiece;
				if (crate.getState() != Crate.STATE_DESTROYED) {
					retVal = CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			}
		}
		return retVal;
	}

}
