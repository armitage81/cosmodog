package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.Tiles;
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
		
		CollisionStatus retVal = CollisionStatus.instance(actor, map, tileX, tileY, true, CollisionStatus.NO_PASSAGE_REASON_NO_REASON);
		
		int roadObjectsTileId = map.getTileId(tileX, tileY, Layers.LAYER_ROADS_OBJECTS_MANUAL);
		
		if (Tiles.ENERGY_WALL_GENERATOR_TILE_ID == roadObjectsTileId) {
		
			int energyWallTileId = map.getTileId(tileX, tileY, Layers.LAYER_META_EFFECTS);
			
			int collectedInfobits = ((Player)actor).getGameProgress().getInfobits();
			if (collectedInfobits < Mappings.ENERGY_WALL_COSTS.get(energyWallTileId)) {
				retVal = CollisionStatus.instance(actor, map, tileX, tileY, false, CollisionStatus.NO_PASSAGE_REASON_ENERGY_WALL_COSTS);
			}
			
		}
		return retVal;
		
	}

}
