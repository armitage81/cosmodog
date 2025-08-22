package antonafanasjew.cosmodog.collision.validators.npc;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerDescriptor;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

/**
 * This validator is checking only the occupied platform as obstacle for enemies (They cannot enter it at all) 
 */
public class PlayerInPlatformAsObstacleCollisionValidatorForNpc extends AbstractCollisionValidator {

	private final Entrance playersTargetEntrance;

	public PlayerInPlatformAsObstacleCollisionValidatorForNpc(Entrance playersTargetEntrance) {
		this.playersTargetEntrance = playersTargetEntrance;
	}
	
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		
		boolean blocked = false;
		
		PlatformInventoryItem platformInventoryItem = (PlatformInventoryItem)ApplicationContextUtils.getPlayer().getInventory().get(InventoryItemType.PLATFORM);
		
		if (platformInventoryItem != null) { //That is, the player is "having" it, or sitting in it, and hence being the obstacle itself
			Position firstPathPosition = playersTargetEntrance.getPosition();
			blocked = CosmodogMapUtils.isTileOnPlatform(entrance.getPosition(), firstPathPosition);
		}
		return CollisionStatus.instance(actor, map, entrance, !blocked, PassageBlockerDescriptor.fromPassageBlockerType(blocked ? PassageBlockerType.BLOCKED : PassageBlockerType.PASSABLE));
		
	}

}
