package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

/**
 * This validator is checking only the occupied platform as obstacle for enemies (They cannot enter it at all) 
 */
public class PlayerInPlatformAsObstacleCollisionValidator extends AbstractCollisionValidator {

	private MovementActionResult playerMovementActionResult;

	public PlayerInPlatformAsObstacleCollisionValidator(MovementActionResult playerMovementActionResult) {
		this.playerMovementActionResult = playerMovementActionResult;
	}
	
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		
		boolean blocked = false;
		
		PlatformInventoryItem platformInventoryItem = (PlatformInventoryItem)ApplicationContextUtils.getPlayer().getInventory().get(InventoryItemType.PLATFORM);
		
		if (platformInventoryItem != null) { //That is, the player is "having" it, or sitting in it, and hence being the obstacle itself 
			blocked = CosmodogMapUtils.isTileOnPlatform(tileX, tileY, playerMovementActionResult.getPath().getX(1), playerMovementActionResult.getPath().getY(1));
		}
		return CollisionStatus.instance(actor, map, tileX, tileY, !blocked, PassageBlockerDescriptor.fromPassageBlockerType(blocked ? PassageBlockerType.BLOCKED : PassageBlockerType.PASSABLE));
		
	}

}
