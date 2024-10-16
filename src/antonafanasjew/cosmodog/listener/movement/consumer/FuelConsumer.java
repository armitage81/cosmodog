package antonafanasjew.cosmodog.listener.movement.consumer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;

public class FuelConsumer implements ResourceConsumer {

	private static final int ONROAD_FUEL_CONSUMPTION_PER_TURN = 1;
	private static final int ROUGH_TERRAN_FUEL_CONSUMPTION_PER_TURN = ONROAD_FUEL_CONSUMPTION_PER_TURN * 3;
	
	@Override
	public int turnCosts(Position position1, Position position2, Player player, CosmodogMap map, ApplicationContext cx) {
		
		//No fuel costs if no movement.
		if (position1.equals(position2)) {
			return 0;
		}
		
		int tileId = map.getTileId(position2, Layers.LAYER_META_TERRAINTYPES);
		boolean onRoad = TileType.getByLayerAndTileId(Layers.LAYER_META_TERRAINTYPES, tileId).equals(TileType.TERRAIN_TYPE_ROAD);
		
		return onRoad ? ONROAD_FUEL_CONSUMPTION_PER_TURN : ROUGH_TERRAN_FUEL_CONSUMPTION_PER_TURN;
		
	}

}
