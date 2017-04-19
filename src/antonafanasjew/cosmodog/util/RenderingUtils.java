package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;

public class RenderingUtils {

	public static boolean isActorOnGroundTypeTile(TileType tileType, CosmodogMap map, Actor actor, ActorTransition actorTransition) {
		boolean retVal = false;
		if (actorTransition == null) {
			int tileId = map.getTileId(actor.getPositionX(), actor.getPositionY(), Layers.LAYER_META_GROUNDTYPES);
			retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(tileType);
		} else {
			int startTileId = map.getTileId(actorTransition.getTransitionalPosX(), actorTransition.getTransitionalPosY(), Layers.LAYER_META_GROUNDTYPES);
			int targetTileId = map.getTileId(actorTransition.getTargetPosX(), actorTransition.getTargetPosY(), Layers.LAYER_META_GROUNDTYPES);
			
			boolean startTileIdIsHighGrassTile = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, startTileId).equals(tileType);
			boolean targetTileIdIsHighGrassTile = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, targetTileId).equals(tileType);
			
			
			if (startTileIdIsHighGrassTile && targetTileIdIsHighGrassTile) {
				retVal = true;
			} else if (!startTileIdIsHighGrassTile && !targetTileIdIsHighGrassTile) {
				retVal = false;
			} else if (startTileIdIsHighGrassTile && !targetTileIdIsHighGrassTile) {
				float transitionalOffset = actorTransition.getTransitionalOffsetX() + actorTransition.getTransitionalOffsetY();
				retVal = transitionalOffset > -0.25 && transitionalOffset < 0.25;
			} else if (!startTileIdIsHighGrassTile && targetTileIdIsHighGrassTile) {
				float transitionalOffset = actorTransition.getTransitionalOffsetX() + actorTransition.getTransitionalOffsetY();
				retVal = transitionalOffset > 0.5 || transitionalOffset < -0.5;
			}
		}
		
		return retVal;
	}
	
	public static boolean isActorOnSoftGroundType(CosmodogMap map, Actor actor, ActorTransition actorTransition) {
		boolean retVal = false;
		if (actorTransition == null) {
			int tileId = map.getTileId(actor.getPositionX(), actor.getPositionY(), Layers.LAYER_META_GROUNDTYPES);
			retVal = tileIdRepresentsSoftGroundType(tileId);
			
		} else {
			
			int startTileId = map.getTileId(actorTransition.getTransitionalPosX(), actorTransition.getTransitionalPosY(), Layers.LAYER_META_GROUNDTYPES);
			boolean startTileSoft = tileIdRepresentsSoftGroundType(startTileId);
			
			int targetTileId = map.getTileId(actorTransition.getTargetPosX(), actorTransition.getTargetPosY(), Layers.LAYER_META_GROUNDTYPES);
			boolean targetTileSoft = tileIdRepresentsSoftGroundType(targetTileId);
			
			
			if (startTileSoft && targetTileSoft) {
				retVal = true;
			} else if (!startTileSoft && !targetTileSoft) {
				retVal = false;
			} else if (startTileSoft && !targetTileSoft) {
				float transitionalOffset = actorTransition.getTransitionalOffsetX() + actorTransition.getTransitionalOffsetY();
				retVal = transitionalOffset > -0.25 && transitionalOffset < 0.25;
			} else if (!startTileSoft && targetTileSoft) {
				float transitionalOffset = actorTransition.getTransitionalOffsetX() + actorTransition.getTransitionalOffsetY();
				retVal = transitionalOffset > 0.5 || transitionalOffset < -0.5;
			}
		}
		
		return retVal;
	}
	
	public static boolean tileIdRepresentsSoftGroundType(int tileId) {
		return
		TileType.GROUND_TYPE_SNOW.getTileId() == tileId
		||
		TileType.GROUND_TYPE_GRASS.getTileId() == tileId
		||
		TileType.GROUND_TYPE_SAND.getTileId() == tileId
		||
		TileType.GROUND_TYPE_SWAMP.getTileId() == tileId;
	}
	
}
