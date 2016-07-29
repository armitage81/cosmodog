package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.domains.ChaussieType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.NpcActor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.tiledmap.TiledMapLayer;
import antonafanasjew.cosmodog.tiledmap.TiledTile;

public class DefaultTravelTimeCalculator extends AbstractTravelTimeCalculator {

	@Override
	protected int calculateTravelTimeInternal(ApplicationContext context, Actor actor, int x, int y) {
		
		CustomTiledMap tiledMap = context.getCustomTiledMap();
		
		TiledMapLayer terrainTypesLayer = tiledMap.getMapLayers().get(Layers.LAYER_META_TERRAINTYPES);
		TiledTile tile = terrainTypesLayer.getTile(x, y);
		int terrainTypeTileId = tile.getGid();
		
		
		if (actor instanceof Player) {

			Player player = (Player) actor;

			int tileId = tiledMap.getTileId(x, y, Layers.LAYER_META_GROUNDTYPES);
			boolean isOnSnowGround = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_SNOW);
			boolean hasSki = player.getInventory().get(InventoryItemType.SKI) != null;
			
			if (hasSki && isOnSnowGround) {
				return costsForActorOnSkiInSnow();
			} else if (player.getInventory().hasVehicle() && !player.getInventory().exitingVehicle()) {
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
	
	private int costsForActorOnSkiInSnow() {
		return Constants.DEFAULT_TIME_COSTS_ON_FOOT;
	}

	private int costsForActorOnWings(int terrainTypeTileId) {
		return Constants.DEFAULT_TIME_COSTS_FLYING;
	}


	private int costsForActorOnTracks(int terrainTypeTileId) {
		return Constants.DEFAULT_TIME_COSTS_WITH_VEHICLE;
	}


	private int costsForActorOnFoot(int terrainTypeTileId) {
		
		if (TileType.TERRAIN_TYPE_WATER.getTileId() == terrainTypeTileId) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT;
		}
		
		if (TileType.TERRAIN_TYPE_ROAD.getTileId() == terrainTypeTileId) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT;
		}
		
		if (TileType.TERRAIN_TYPE_PLAIN.getTileId() == terrainTypeTileId) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT;
		}
		
		if (TileType.TERRAIN_TYPE_UNEVEN.getTileId() == terrainTypeTileId) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT;
		}
		
		if (TileType.TERRAIN_TYPE_ROUGH.getTileId() == terrainTypeTileId) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT * 2;
		}
		
		if (TileType.TERRAIN_TYPE_BROKEN.getTileId() == terrainTypeTileId) {
			return Constants.DEFAULT_TIME_COSTS_ON_FOOT * 3;
		}
		
		return 0;
		
	}
	
	private int costsForActorOnWheels(int terrainTypeTileId) {
		if (TileType.TERRAIN_TYPE_ROAD.getTileId() == terrainTypeTileId) {
			return Constants.DEFAULT_TIME_COSTS_WITH_VEHICLE;
		}
		
		if (TileType.TERRAIN_TYPE_PLAIN.getTileId() == terrainTypeTileId) {
			return Constants.DEFAULT_TIME_COSTS_WITH_VEHICLE * 2;
		}
		
		return 0;
	}

}
