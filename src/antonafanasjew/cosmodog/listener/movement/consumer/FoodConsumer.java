package antonafanasjew.cosmodog.listener.movement.consumer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.structures.MoveableGroup;

public class FoodConsumer implements ResourceConsumer {

	private static final int DEFAULT_FOOD_CONSUMPTION_PER_TURN = 2;
	private static final int FOOD_CONSUMPTION_PER_TURN_WHEN_FOUND_NUTRIENTS = 1;
	
	@Override
	public int turnCosts(int x1, int y1, int x2, int y2, Player player, CosmodogMap map, ApplicationContext cx) {
		
		boolean hasVehicle = player.getInventory().get(InventoryItemType.VEHICLE) != null;
		boolean hasPlatform = player.getInventory().get(InventoryItemType.PLATFORM) != null;
		MoveableGroup moveableGroupAroundPlayer = PlayerMovementCache.getInstance().getActiveMoveableGroup();
		boolean inRiddleArea = moveableGroupAroundPlayer != null && moveableGroupAroundPlayer.isResetable();

		if (hasVehicle || hasPlatform || inRiddleArea) {
			return 0;
		}

		boolean foundNutrients = player.getInventory().get(InventoryItemType.NUTRIENTS) != null;
		return foundNutrients ? FOOD_CONSUMPTION_PER_TURN_WHEN_FOUND_NUTRIENTS : DEFAULT_FOOD_CONSUMPTION_PER_TURN;
	}

}
