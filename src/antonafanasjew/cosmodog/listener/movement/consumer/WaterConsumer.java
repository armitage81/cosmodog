package antonafanasjew.cosmodog.listener.movement.consumer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class WaterConsumer implements ResourceConsumer {

	private static final int DEFAULT_WATER_CONSUMPTION_PER_TURN = Constants.MINUTES_PER_TURN;
	private static final int DESERT_WATER_CONSUMPTION_PER_TURN = DEFAULT_WATER_CONSUMPTION_PER_TURN * 3;
	
	@Override
	public int turnCosts(int x1, int y1, int x2, int y2, Player player, CosmodogMap map, ApplicationContext cx) {
		
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		
		boolean hasVehicle = player.getInventory().get(InventoryItemType.VEHICLE) != null;
		boolean hasPlatform = player.getInventory().get(InventoryItemType.PLATFORM) != null;
		MoveableGroup moveableGroupAroundPlayer = PlayerMovementCache.getInstance().getActiveMoveableGroup();
		boolean inRiddleArea = moveableGroupAroundPlayer != null && moveableGroupAroundPlayer.isResetable();
		
		if (hasVehicle || hasPlatform || inRiddleArea) {
			return 0;
		}
		
		int tileId = map.getTileId(x2, y2, Layers.LAYER_META_GROUNDTYPES);
		
		boolean onSand = TileType.GROUND_TYPE_SAND.getTileId() == tileId;
		boolean atNight = game.getPlanetaryCalendar().isNight();

		int turnCosts;
		if (onSand) {
			turnCosts = atNight ? DEFAULT_WATER_CONSUMPTION_PER_TURN : DESERT_WATER_CONSUMPTION_PER_TURN;
		} else {
			turnCosts = DEFAULT_WATER_CONSUMPTION_PER_TURN;
		}

		return turnCosts;
		
	}

}
