package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.Mappings;

/**
 * Defines collision of energy walls based on the collected infobits and the costs for passage.  
 */
public class EnergyWallCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		
		CollisionStatus retVal = CollisionStatus.instance(actor, map, tileX, tileY, true, PassageBlockerType.PASSABLE);
		
		int roadObjectsTileId = map.getTileId(tileX, tileY, Layers.LAYER_ROADS_OBJECTS_MANUAL);
		
		if (TileType.ENERGY_WALL_GENERATOR.getTileId() == roadObjectsTileId) {
		
			int energyWallTileId = map.getTileId(tileX, tileY, Layers.LAYER_META_EFFECTS);
			
			int collectedInfobits = ((Player)actor).getGameProgress().getInfobits();
			int requiredInfobits = Mappings.ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.get(TileType.getByLayerAndTileId(Layers.LAYER_META_EFFECTS, energyWallTileId));
			if (collectedInfobits < requiredInfobits) {
				retVal = CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.ENERGY_WALL_COSTS, String.valueOf(requiredInfobits));
			}
			
		}
		return retVal;
		
	}

}
