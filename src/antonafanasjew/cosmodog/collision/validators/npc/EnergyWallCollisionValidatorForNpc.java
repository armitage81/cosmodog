package antonafanasjew.cosmodog.collision.validators.npc;

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

public class EnergyWallCollisionValidatorForNpc extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		
		CollisionStatus retVal = CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);
		
		int roadObjectsTileId = map.getTileId(entrance.getPosition(), Layers.LAYER_ROADS_OBJECTS_MANUAL);
		
		if (TileType.ENERGY_WALL_GENERATOR.getTileId() == roadObjectsTileId) {
		
			retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED);

		}
		return retVal;
		
	}

}
