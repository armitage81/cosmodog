package antonafanasjew.cosmodog.listener.movement.consumer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;

public class FoodConsumer implements ResourceConsumer {

	private static final int DEFAULT_FOOD_CONSUMPTION_PER_TURN = Constants.MINUTES_PER_TURN;
	
	@Override
	public int turnCosts(int x1, int y1, int x2, int y2, Player player, CosmodogMap map, ApplicationContext cx) {
		
		boolean hasVehicle = player.getInventory().get(InventoryItemType.VEHICLE) != null;
		boolean hasPlatform = player.getInventory().get(InventoryItemType.PLATFORM) != null;
		
		if (hasVehicle || hasPlatform) {
			return 0;
		}
		
		return DEFAULT_FOOD_CONSUMPTION_PER_TURN;
	}

}
