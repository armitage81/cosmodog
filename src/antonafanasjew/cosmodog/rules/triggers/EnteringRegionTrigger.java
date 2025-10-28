package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.MapDescriptor;
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

	private MapDescriptor mapDescriptor;
	private String layerName;
	private String regionName;
	
	public EnteringRegionTrigger(MapDescriptor mapDescriptor, String layerName, String regionName) {
		this.mapDescriptor = mapDescriptor;
		this.layerName = layerName;
		this.regionName = regionName;
	}

	@Override
	public boolean accept(GameEvent event) {

		if (!(event instanceof GameEventChangedPosition)) {
			return false;
		}

		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(mapDescriptor);
		
		Player player = ApplicationContextUtils.getPlayer();
		
		TiledObjectGroup regionsObjectGroup = map.getObjectGroups().get(layerName);
		
		TiledObject regionObject = regionsObjectGroup.getObjects().get(regionName);
		
		if (regionObject != null) {
			return RegionUtils.pieceInRegion(player, mapDescriptor, regionObject);
		}
		
		return false;
		
		
		
	}

}
