package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * This validator blocks every tile that is unpassable (red collision tiles).
 */
public class UnpassableCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		boolean collisionTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION);
		boolean passable = !collisionTile;
		PassageBlockerType passageBlocker = passable ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED;
		return CollisionStatus.instance(actor, map, tileX, tileY, passable, passageBlocker);
		
	}

}
