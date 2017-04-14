package antonafanasjew.cosmodog.actions.teleportation;

import java.util.Collection;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.dynamicpieces.Poison;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject.Point;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CollisionUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

public class ActualPoisonDeactivationActionPhase extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -7801472349956706302L;

	private TiledPolylineObject poisonSwitchConnection;
	private boolean executedAction = false;
	
	public ActualPoisonDeactivationActionPhase(int duration, TiledPolylineObject poisonSwitchConnection) {
		super(duration);
		this.poisonSwitchConnection = poisonSwitchConnection;
	}

	@Override
	public void onTrigger() {
		
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		if (after < getDuration() / 2) {
			return;
		}
		
		
		
		
		if (!executedAction) {
			
			executedAction = true;

			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_DRAIN_POISON).play();
			
			CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
			
			Point endPoint = poisonSwitchConnection.getPoints().get(1);
			PlacedRectangle rectangleAroundEndPoint = PlacedRectangle.fromAnchorAndSize(endPoint.x, endPoint.y, 1, 1);
			
	
			TiledObjectGroup poisonsRegionsObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_POISON_REGIONS);
			
			Map<String, TiledObject> poisonRegionObjects = poisonsRegionsObjectGroup.getObjects();
			
			TiledObject relevantPoisonRegion = null;
			
			for (String poisonRegionName : poisonRegionObjects.keySet()) {
				TiledObject poisonRegion = poisonRegionObjects.get(poisonRegionName);
				if (CollisionUtils.intersects(rectangleAroundEndPoint, poisonRegion)) {
					relevantPoisonRegion = poisonRegion;
					break;
				}
			}
			
			if (relevantPoisonRegion != null) {
				
				Collection<DynamicPiece> poisons = map.getDynamicPieces().get(Poison.class);
				
				for (DynamicPiece piece : poisons) {
					Poison poison = (Poison)piece;
					if (RegionUtils.pieceInRegion(poison, relevantPoisonRegion, map.getTileWidth(), map.getTileHeight())) {
						poison.setState(Poison.STATE_DEACTIVATED);
					}
				}
			}
		
		}
		
	}
	
	@Override
	public void onEnd() {

		
	}

}
