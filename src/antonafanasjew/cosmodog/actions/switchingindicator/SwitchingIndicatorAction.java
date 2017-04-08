package antonafanasjew.cosmodog.actions.switchingindicator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.dynamicpieces.BinaryIndicator;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

import com.google.common.collect.Sets;

public class SwitchingIndicatorAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -8462906572353901070L;

	private String regionName;
	
	private Set<BinaryIndicator> indicatorsInRegion = Sets.newHashSet();
	private boolean onNotOff;
	
	public SwitchingIndicatorAction(int duration, String regionName, boolean onNotOff) {
		super(duration);
		this.regionName = regionName;
		this.onNotOff = onNotOff;
	}

	public String getRegionName() {
		return regionName;
	}

	@Override
	public void onTrigger() {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		TiledObjectGroup regions = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_REGIONS);
		TiledObject region = regions.getObjects().get(regionName);
		Collection<DynamicPiece> gates = map.getDynamicPieces().get(BinaryIndicator.class);
		Iterator<DynamicPiece> it = gates.iterator();
		while(it.hasNext()) {
			BinaryIndicator binaryIndicator = (BinaryIndicator)it.next();
			if (RegionUtils.pieceInRegion(binaryIndicator, region, map.getTileWidth(), map.getTileHeight())) {
				indicatorsInRegion.add(binaryIndicator);
			}
		}
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		
		for (BinaryIndicator binaryIndicator : indicatorsInRegion) {
			
			if (actionPercentage >= 0.5) {
				binaryIndicator.setState(onNotOff ? BinaryIndicator.STATE_TRUE : BinaryIndicator.STATE_FALSE);
			}
			
		}
	}

}
