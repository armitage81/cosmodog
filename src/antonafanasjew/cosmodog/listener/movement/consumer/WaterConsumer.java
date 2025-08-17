package antonafanasjew.cosmodog.listener.movement.consumer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.structures.SafeSpace;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class WaterConsumer implements ResourceConsumer {

	private static final int DEFAULT_WATER_CONSUMPTION_PER_TURN = Constants.MINUTES_PER_TURN;
	private static final int DESERT_WATER_CONSUMPTION_PER_TURN = DEFAULT_WATER_CONSUMPTION_PER_TURN * 3;
	
	@Override
	public int turnCosts(Position position1, Position position2, Player player, CosmodogMap map, ApplicationContext cx) {
		
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		
		boolean hasVehicle = player.getInventory().get(InventoryItemType.VEHICLE) != null;
		boolean hasPlatform = player.getInventory().get(InventoryItemType.PLATFORM) != null;
		MoveableGroup moveableGroupAroundPlayer = PlayerMovementCache.getInstance().getActiveMoveableGroup();
		boolean inSocobanArea = moveableGroupAroundPlayer != null && moveableGroupAroundPlayer.isResetable();
		SafeSpace safeSpaceAroundPlayer = PlayerMovementCache.getInstance().getActiveSafeSpace();
		boolean inSafeSpace = safeSpaceAroundPlayer != null;

		if (hasPlatform || inSocobanArea || inSafeSpace) {
			return 0;
		}

		//The player has the access to the car compartment only if this flag is set. (It is set when the car race is won in the North-West of the map.)
		if (hasVehicle && player.getGameProgress().getProgressProperties().get(GameProgress.GAME_PROGRESS_PROPERTY_ACCESSTOCARCOMPARTMENT) != null) {
			return 0;
		}

		int tileId = map.getTileId(position2, Layers.LAYER_META_GROUNDTYPES);
		
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
