package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventChangedPosition;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;
import antonafanasjew.cosmodog.util.TileUtils;

/**
 * Indicates whether the player has entered a region as defined on the tiled map under given layer and name
 */
public class EnteringRegionTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = 1280404774181149596L;

	private MapType mapType;
	private String layerName;
	private String regionName;
	
	public EnteringRegionTrigger(MapType mapType, String layerName, String regionName) {
		this.mapType = mapType;
		this.layerName = layerName;
		this.regionName = regionName;
	}

	@Override
	public boolean accept(GameEvent event) {

		if (!(event instanceof GameEventChangedPosition)) {
			return false;
		}

		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(mapType);
		
		Player player = ApplicationContextUtils.getPlayer();
		
		TiledObjectGroup regionsObjectGroup = map.getObjectGroups().get(layerName);
		
		TiledObject regionObject = regionsObjectGroup.getObjects().get(regionName);
		
		if (regionObject != null) {
			return RegionUtils.pieceInRegion(player, mapType, regionObject);
		}
		
		return false;
		
		
		
	}

}
