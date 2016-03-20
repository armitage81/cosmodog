package antonafanasjew.cosmodog.collision;

import java.util.List;

import org.newdawn.slick.tiled.TiledMap;

import antonafanasjew.cosmodog.model.actors.Actor;

import com.google.common.collect.Lists;

public class DefaultWaterValidator extends AbstractWaterValidator {

	private static final int WATER_LAYER_ID = 20;
	private static final List<Integer> WATER_TILE_IDS = Lists.newArrayList();
		
	static {
		WATER_TILE_IDS.add(73);
	}
	
	@Override
	protected boolean waterInReachInternal(Actor actor, TiledMap map, int tileX, int tileY) {

		List<Integer> tileIds = Lists.newArrayList();

		//Collect all neighboring tile ids and the actual position tile id
		for (int i = actor.getPositionX() - 1; i <= actor.getPositionX() + 1; i++) {
			for (int j = actor.getPositionY() - 1; j <= actor.getPositionY() + 1; j++) {
				
				if (i >= 0 && i < map.getWidth() && j >=0 && j < map.getHeight()) {
					tileIds.add(map.getTileId(i, j, WATER_LAYER_ID));
				}
			}
		}
		
		//Check for each tile ids if they contain water (or snow)
		
		for (int tileId : tileIds) {
			if (WATER_TILE_IDS.contains(tileId)) {
				return true;
			}
		}
		
		return false;
		
		
	}

}
