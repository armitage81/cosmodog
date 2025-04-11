package antonafanasjew.cosmodog.collision.validators.player;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.Mappings;

/**
 * Defines collision of energy walls based on the collected infobits and the costs for passage.  
 */
public class EnergyWallCollisionValidatorForPlayer extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		
		CollisionStatus retVal = CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);
		
		int roadObjectsTileId = map.getTileId(entrance.getPosition(), Layers.LAYER_ROADS_OBJECTS_MANUAL);
		
		if (TileType.ENERGY_WALL_GENERATOR.getTileId() == roadObjectsTileId) {
		
			int energyWallTileId = map.getTileId(entrance.getPosition(), Layers.LAYER_META_EFFECTS);
			
			int collectedInfobits = ((Player)actor).getGameProgress().getInfobits();
			int requiredInfobits = Mappings.ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.get(TileType.getByLayerAndTileId(Layers.LAYER_META_EFFECTS, energyWallTileId));
			if (collectedInfobits < requiredInfobits) {
				retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.ENERGY_WALL_COSTS, String.valueOf(requiredInfobits));
			}
			
		}
		return retVal;
		
	}

}
