package antonafanasjew.cosmodog.listener.movement.consumer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.structures.SafeSpace;
import antonafanasjew.cosmodog.topology.Position;

public class FoodConsumer implements ResourceConsumer {

	private static final int DEFAULT_FOOD_CONSUMPTION_PER_TURN = 2;
	private static final int FOOD_CONSUMPTION_PER_TURN_WHEN_FOUND_NUTRIENTS = 1;
	
	@Override
	public int turnCosts(Position position1, Position position2, Player player, CosmodogMap map, ApplicationContext cx) {
		
		boolean hasVehicle = player.getInventory().get(InventoryItemType.VEHICLE) != null;
		boolean hasPlatform = player.getInventory().get(InventoryItemType.PLATFORM) != null;
		MoveableGroup moveableGroupAroundPlayer = PlayerMovementCache.getInstance().getActiveMoveableGroup();
		boolean inSocobanArea = moveableGroupAroundPlayer != null && moveableGroupAroundPlayer.isResetable();

		SafeSpace safeSpaceAroundPlayer = PlayerMovementCache.getInstance().getActiveSafeSpace();
		boolean inSafeSpace = safeSpaceAroundPlayer != null;

		if (hasVehicle || hasPlatform || inSocobanArea || inSafeSpace) {
			return 0;
		}

		boolean foundNutrients = player.getInventory().get(InventoryItemType.NUTRIENTS) != null;
		return foundNutrients ? FOOD_CONSUMPTION_PER_TURN_WHEN_FOUND_NUTRIENTS : DEFAULT_FOOD_CONSUMPTION_PER_TURN;
	}

}
