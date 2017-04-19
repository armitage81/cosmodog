package antonafanasjew.cosmodog.actions.loweringgate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.dynamicpieces.Gate;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

import com.google.common.collect.Sets;

public class LowerGateAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -8462906572353901070L;

	private String regionName;
	
	private Set<Gate> gatesInRegion = Sets.newHashSet();
	
	public LowerGateAction(int duration, String regionName) {
		super(duration);
		this.regionName = regionName;
	}

	public String getRegionName() {
		return regionName;
	}

	@Override
	public void onTrigger() {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		TiledObjectGroup regions = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_REGIONS);
		TiledObject region = regions.getObjects().get(regionName);
		Collection<DynamicPiece> gates = map.getDynamicPieces().get(Gate.class);
		Iterator<DynamicPiece> it = gates.iterator();
		while(it.hasNext()) {
			Gate gate = (Gate)it.next();
			if (RegionUtils.pieceInRegion(gate, region, map.getTileWidth(), map.getTileHeight())) {
				gatesInRegion.add(gate);
			}
		}
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		
		for (Gate gate : gatesInRegion) {
			
			if (actionPercentage >= 0.15 && actionPercentage < 0.3) {
				gate.setState(Gate.STATE_LOWERING_PHASE1);
			}
			
			if (actionPercentage >= 0.3 && actionPercentage < 0.45) {
				gate.setState(Gate.STATE_LOWERING_PHASE2);
			}
			
			if (actionPercentage >= 0.45 && actionPercentage < 0.6) {
				gate.setState(Gate.STATE_LOWERING_PHASE3);
			}
			
			if (actionPercentage >= 0.6 && actionPercentage < 0.75) {
				gate.setState(Gate.STATE_LOWERING_PHASE4);
			}
			
			if (actionPercentage >= 0.75) {
				gate.setState(Gate.STATE_LOWERED);
			}
		}
	}
	
	@Override
	public void onEnd() {
		for (Gate gate : gatesInRegion) {
			gate.setState(Gate.STATE_LOWERED);
		}
	}
}
