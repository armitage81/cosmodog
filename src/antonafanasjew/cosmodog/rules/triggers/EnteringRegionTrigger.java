package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventChangedPosition;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

/**
 * Indicates whether the player has entered a region as defined on the tiled map under given layer and name
 */
public class EnteringRegionTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = 1280404774181149596L;

	private String layerName;
	private String regionName;
	
	public EnteringRegionTrigger(String layerName, String regionName) {
		this.layerName = layerName;
		this.regionName = regionName;
	}

	@Override
	public boolean accept(GameEvent event) {
		
		if (event instanceof GameEventChangedPosition == false) {
			return false;
		}
		
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		Player player = ApplicationContextUtils.getPlayer();
		
		TiledObjectGroup regionsObjectGroup = map.getObjectGroups().get(layerName);
		
		TiledObject regionObject = regionsObjectGroup.getObjects().get(regionName);
		
		if (regionObject != null) {
			return RegionUtils.pieceInRegion(player, regionObject, map.getTileWidth(), map.getTileHeight());
		}
		
		return false;
		
		
		
	}

}
