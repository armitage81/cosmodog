package antonafanasjew.cosmodog.collision.validators.npc;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerDescriptor;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

/**
 * This validator is checking only the occupied platform as obstacle for enemies (They cannot enter it at all) 
 */
public class PlayerInPlatformAsObstacleCollisionValidatorForNpc extends AbstractCollisionValidator {

	private final MovementActionResult playerMovementActionResult;

	public PlayerInPlatformAsObstacleCollisionValidatorForNpc(MovementActionResult playerMovementActionResult) {
		this.playerMovementActionResult = playerMovementActionResult;
	}
	
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		
		boolean blocked = false;
		
		PlatformInventoryItem platformInventoryItem = (PlatformInventoryItem)ApplicationContextUtils.getPlayer().getInventory().get(InventoryItemType.PLATFORM);
		
		if (platformInventoryItem != null) { //That is, the player is "having" it, or sitting in it, and hence being the obstacle itself
			Position firstPathPosition = Position.fromCoordinates(playerMovementActionResult.getPath().getX(1), playerMovementActionResult.getPath().getY(1));
			blocked = CosmodogMapUtils.isTileOnPlatform(position, firstPathPosition);
		}
		return CollisionStatus.instance(actor, map, position, !blocked, PassageBlockerDescriptor.fromPassageBlockerType(blocked ? PassageBlockerType.BLOCKED : PassageBlockerType.PASSABLE));
		
	}

}
