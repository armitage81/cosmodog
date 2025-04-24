package antonafanasjew.cosmodog.collision.validators.player;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.MoveableDynamicPiece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.AlienBaseBlockade;
import antonafanasjew.cosmodog.model.dynamicpieces.Bamboo;
import antonafanasjew.cosmodog.model.dynamicpieces.BinaryIndicator;
import antonafanasjew.cosmodog.model.dynamicpieces.Crate;
import antonafanasjew.cosmodog.model.dynamicpieces.CrumbledWall;
import antonafanasjew.cosmodog.model.dynamicpieces.Door;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.dynamicpieces.Gate;
import antonafanasjew.cosmodog.model.dynamicpieces.HardStone;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.dynamicpieces.Poison;
import antonafanasjew.cosmodog.model.dynamicpieces.PressureButton;
import antonafanasjew.cosmodog.model.dynamicpieces.SecretDoor;
import antonafanasjew.cosmodog.model.dynamicpieces.Stone;
import antonafanasjew.cosmodog.model.dynamicpieces.Terminal;
import antonafanasjew.cosmodog.model.dynamicpieces.Tree;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Bollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.OneWayBollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Reflector;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Sensor;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.KeyRingInventoryItem;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.model.upgrades.Key;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PositionUtils;

import java.util.Optional;
import java.util.Set;

public class DynamicPieceCollisionValidatorForPlayer extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);

		//In case there are multiple dynamic pieces at one position, we assume that a moveable is on a non-blocking dynamic piece, such as a sensor.
		//In this case we assume that we should move the moveable and ignore the other dynamic pieces. (If a moveable is on it, player will also be able to enter anyway.)
		Set<DynamicPiece> dynamicPieces = map.dynamicPiecesAtPosition(entrance.getPosition());
		Optional<MoveableDynamicPiece> optMoveable = dynamicPieces.stream().filter(e -> e instanceof MoveableDynamicPiece).map(e -> (MoveableDynamicPiece)e).findFirst();
		DynamicPiece dynamicPiece = optMoveable.isPresent() ? optMoveable.get() : dynamicPieces.stream().findFirst().orElse(null);

		if (dynamicPiece != null) {
			if (dynamicPiece instanceof Sensor) {
				//Do nothing. Just a place holder to not forget this part in case something changes.
			}
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
				
				//The player can move at the position of a moveable dynamic piece only if this piece can be moved away from the desired location.
				MoveableDynamicPiece moveable = (MoveableDynamicPiece)dynamicPiece;
				Actor moveableActor = moveable.asActor();
				CollisionValidator collisionValidatorForMoveable = ApplicationContextUtils.getCosmodog().getCollisionValidatorForMoveable();

				Entrance moveableTargetEntrance = cosmodogGame.targetEntrance(moveableActor, entrance.getEntranceDirection());

				CollisionStatus collisionStatusForMoveable = collisionValidatorForMoveable.collisionStatus(cosmodogGame, moveableActor, map, moveableTargetEntrance);
				if (collisionStatusForMoveable.isPassable()) {
					retVal = CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);
				} else {
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
				
			} else if (dynamicPiece instanceof Terminal) {
				DirectionType directionType = PositionUtils.targetDirection(actor, dynamicPiece);
				String blockReasonParam = directionType == DirectionType.UP ? "" : "Blocked";
				retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
			} else if (dynamicPiece instanceof BinaryIndicator) {
				retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
			} else if (dynamicPiece instanceof Stone) {
				Stone stone = (Stone)dynamicPiece;
				if (stone.getState() != Stone.STATE_DESTROYED) {
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof HardStone) {
				HardStone hardStone = (HardStone)dynamicPiece;
				if (hardStone.getState() != Stone.STATE_DESTROYED) {
					String blockReasonParam = "<knock>";
					Player player = (Player)actor;
					Inventory inventory = player.getInventory();
					if (inventory.get(InventoryItemType.PICK) == null) {
						blockReasonParam = "Requires pick";
					}
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof Tree) {
				Tree tree = (Tree)dynamicPiece;
				if (tree.getState() != Tree.STATE_DESTROYED) {
					String blockReasonParam = "<knock>";
					Player player = (Player)actor;
					Inventory inventory = player.getInventory();
					if (inventory.get(InventoryItemType.AXE) == null) {
						blockReasonParam = "Requires axe";
					}
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof Bamboo) {
				Bamboo bamboo = (Bamboo)dynamicPiece;
				if (bamboo.getState() != Bamboo.STATE_DESTROYED) {
					String blockReasonParam = "<knock>";
					Player player = (Player)actor;
					Inventory inventory = player.getInventory();
					if (inventory.get(InventoryItemType.MACHETE) == null) {
						blockReasonParam = "Requires machete";
					}
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof CrumbledWall) {
				CrumbledWall wall = (CrumbledWall)dynamicPiece;
				if (wall.getState() != CrumbledWall.STATE_DESTROYED) {
					String blockReasonParam = "";
					Player player = (Player)actor;
					Inventory inventory = player.getInventory();
					if (inventory.get(InventoryItemType.DYNAMITE) == null) {
						blockReasonParam = "Requires dynamite";
					}
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof Gate) {
				Gate gate = (Gate)dynamicPiece;
				if (!gate.isLowered()) {
					String blockReasonParam = "Gate is closed";
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof SecretDoor) {
				SecretDoor door = (SecretDoor)dynamicPiece;
				if (!door.isOpen()) {
					String blockReasonParam = "Door is closed";
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof Bollard bollard) {
				if (!bollard.isOpen()) {
					String blockReasonParam = "Door is closed";
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof OneWayBollard oneWayBollard) {
				Player player = ApplicationContextUtils.getPlayer();
				if (player.getDirection() != oneWayBollard.getDirection()) {
					String blockReasonParam = "Wrong side";
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof Crate) {
				Crate crate = (Crate)dynamicPiece;
				if (crate.getState() != Crate.STATE_DESTROYED) {
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
				}
			} else if (dynamicPiece instanceof Door) {
				Door door = (Door)dynamicPiece;
				if (door.closed()) {
					String blockReasonParam = "<swipe>";
					boolean hasRightKey = false; 
					Player player = (Player)actor;
					Inventory inventory = player.getInventory();
					DoorType doorType = door.getDoorType();
					if (inventory.get(InventoryItemType.KEY_RING) != null) {
						KeyRingInventoryItem keyRingInventoryItem = (KeyRingInventoryItem)inventory.get(InventoryItemType.KEY_RING);
						Key doorKey = keyRingInventoryItem.getKeysCopy().get(doorType);
						if (doorKey != null) {
							hasRightKey = true;
						}
					}
					
					if (!hasRightKey) {
						blockReasonParam = "Requires " + doorType.getKeyDescription();
					}
						
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				} 
			} else if (dynamicPiece instanceof AlienBaseBlockade alienBaseBlockade) {
                if (alienBaseBlockade.closed()) {
					String blockReasonParam = "Your alien knowledge opens the door.";
					Player player = (Player) actor;
					Inventory inventory = player.getInventory();
					InsightInventoryItem insightInventoryItem = (InsightInventoryItem) inventory.get(InventoryItemType.INSIGHT);

					if (insightInventoryItem == null || insightInventoryItem.getNumber() < Constants.MIN_INSIGHTS_TO_OPEN_ALIEN_BASE) {
						blockReasonParam = "Requires " +  String.valueOf(Constants.MIN_INSIGHTS_TO_OPEN_ALIEN_BASE) + " insights to open.";
					}
					retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, blockReasonParam);
				}
			} else if (dynamicPiece instanceof Reflector) {
				retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
			}
		}
		return retVal;
	}

}
