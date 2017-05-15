package antonafanasjew.cosmodog.listener.movement.consumer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;

public class FuelConsumer implements ResourceConsumer {

	private static final int ONROAD_FUEL_CONSUMPTION_PER_TURN = 1;
	private static final int ROUGH_TERRAN_FUEL_CONSUMPTION_PER_TURN = ONROAD_FUEL_CONSUMPTION_PER_TURN * 3;
	
	@Override
	public int turnCosts(int x1, int y1, int x2, int y2, Player player, CosmodogMap map, ApplicationContext cx) {
		
		//No fuel costs if no movement.
		if (x1 == x2 && y1 == y2) {
			return 0;
		}
		
		int tileId = map.getTileId(x2, y2, Layers.LAYER_META_TERRAINTYPES);
		boolean onRoad = TileType.getByLayerAndTileId(Layers.LAYER_META_TERRAINTYPES, tileId).equals(TileType.TERRAIN_TYPE_ROAD);
		
		return onRoad ? ONROAD_FUEL_CONSUMPTION_PER_TURN : ROUGH_TERRAN_FUEL_CONSUMPTION_PER_TURN;
		
	}

}
