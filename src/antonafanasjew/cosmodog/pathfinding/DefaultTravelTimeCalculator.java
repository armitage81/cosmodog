package antonafanasjew.cosmodog.pathfinding;

import org.newdawn.slick.tiled.TiledMap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.ChaussieType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.Tiles;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.NpcActor;
import antonafanasjew.cosmodog.model.actors.Player;

public class DefaultTravelTimeCalculator extends AbstractTravelTimeCalculator {

	@Override
	protected int calculateTravelTimeInternal(ApplicationContext context, Actor actor, int x, int y) {
		
		TiledMap tiledMap = context.getTiledMap();
		int terrainTypeTileId = tiledMap.getTileId(x, y, Layers.LAYER_META_TERRAINTYPES);
		
		
		if (actor instanceof Player) {
			
			Player player = (Player) actor;
			
			if (player.getInventory().hasVehicle() && !player.getInventory().exitingVehicle()) {
				return costsForActorOnWheels(terrainTypeTileId);
			} else {
				return costsForActorOnFoot(terrainTypeTileId);
			}
		} else if (actor instanceof NpcActor){
			NpcActor npcActor = (NpcActor)actor;
			ChaussieType chaussieType = npcActor.getChaussieType();
			
			if (chaussieType.equals(ChaussieType.FEET)) {
				return costsForActorOnFoot(terrainTypeTileId);
			} else if (chaussieType.equals(ChaussieType.FLYING)) {
				return costsForActorOnWings(terrainTypeTileId);
			} else if (chaussieType.equals(ChaussieType.SWIMMING)) {
				return costsForActorOnFoot(terrainTypeTileId);
			} else if (chaussieType.equals(ChaussieType.WHEELS)) {
				return costsForActorOnWheels(terrainTypeTileId);
			} else if (chaussieType.equals(ChaussieType.TRACKS)) {
				return costsForActorOnTracks(terrainTypeTileId);
			} else {
				return costsForActorOnFoot(terrainTypeTileId);
			}
			
		} else {
			return costsForActorOnFoot(terrainTypeTileId);
		}
	}
	
	
	private int costsForActorOnWings(int terrainTypeTileId) {
		return Constants.DEFAULT_TIME_COSTS_FLYING;
	}


	private int costsForActorOnTracks(int terrainTypeTileId) {
		return Constants.DEFAULT_TIME_COSTS_WITH_VEHICLE;
	}


	private int costsForActorOnFoot(int terrainTypeTileId) {
		
		if (terrainTypeTileId == Tiles.TERRAIN_TYPE_WATER_TILE_ID) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT;
		}
		
		if (terrainTypeTileId == Tiles.TERRAIN_TYPE_ROAD_TILE_ID) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT;
		}
		
		if (terrainTypeTileId == Tiles.TERRAIN_TYPE_PLAIN_TILE_ID) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT;
		}
		
		if (terrainTypeTileId == Tiles.TERRAIN_TYPE_UNEVEN_TILE_ID) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT;
		}
		
		if (terrainTypeTileId == Tiles.TERRAIN_TYPE_ROUGH_TILE_ID) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT * 2;
		}
		
		if (terrainTypeTileId == Tiles.TERRAIN_TYPE_BROKEN_TILE_ID) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT * 3;
		}
		
		return 0;
		
	}
	
	private int costsForActorOnWheels(int terrainTypeTileId) {
		if (terrainTypeTileId == Tiles.TERRAIN_TYPE_ROAD_TILE_ID) {
			return Constants.DEFAULT_TIME_COSTS_WITH_VEHICLE;
		}
		
		if (terrainTypeTileId == Tiles.TERRAIN_TYPE_PLAIN_TILE_ID) {
			return Constants.DEFAULT_TIME_COSTS_WITH_VEHICLE * 2;
		}
		
		return 0;
	}

}
