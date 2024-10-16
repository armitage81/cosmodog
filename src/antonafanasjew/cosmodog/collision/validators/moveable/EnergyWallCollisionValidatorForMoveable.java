package antonafanasjew.cosmodog.collision.validators.moveable;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Defines collision of energy walls based on the collected infobits and the costs for passage.  
 */
public class EnergyWallCollisionValidatorForMoveable extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		
		CollisionStatus retVal = CollisionStatus.instance(actor, map, position, true, PassageBlockerType.PASSABLE);
		
		int roadObjectsTileId = map.getTileId(position, Layers.LAYER_ROADS_OBJECTS_MANUAL);
		
		if (TileType.ENERGY_WALL_GENERATOR.getTileId() == roadObjectsTileId) {
		
			retVal = CollisionStatus.instance(actor, map, position, false, PassageBlockerType.ENERGY_WALL_COSTS, "");
			
		}
		return retVal;
		
	}

}
