package antonafanasjew.cosmodog.collision;

import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.NpcActor;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CollisionUtils;
import antonafanasjew.cosmodog.util.ObjectGroupUtils;

/**
 * This collision validator will only used for enemies. It will block the passage 
 * if the tile is not part of the enemies home region as defined in the actor object.
 * Enemies without a home region will pass this validator for any tile.
 */
public class NpcHomeRegionCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {

		CollisionStatus passable = CollisionStatus.instance(actor, map, tileX, tileY, true, PassageBlocker.PASSABLE);
		CollisionStatus notPassable = CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlocker.OUT_OF_HOME_REGION);
		
		NpcActor npcActor = (NpcActor)actor;
		String homeRegionName = npcActor.getHomeRegionName();
		
		if (homeRegionName == null) {
			return passable;
		}
		
		CustomTiledMap customTiledMap = ApplicationContextUtils.getCustomTiledMap();
		TiledObject homeRegion = customTiledMap.getObjectGroups().get(ObjectGroupUtils.OBJECT_GROUP_ID_NPC_HOME_REGIONS).getObjects().get(homeRegionName);
		
		if (homeRegion == null) {
			Log.warn("NPC Actor " + npcActor + " has defined a home region name " + homeRegionName + " that does not exist on the map.");
			return passable;
		}
		
		int x = tileX * map.getTileWidth();
		int y = tileY * map.getTileHeight();
		int w = map.getTileWidth();
		int h = map.getTileHeight();
		
		PlacedRectangle r = PlacedRectangle.fromAnchorAndSize(x, y, w, h);
		
		boolean intersects = CollisionUtils.intersects(r, homeRegion);
		
		return intersects ? passable : notPassable;
		
	}

}
