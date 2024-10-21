package antonafanasjew.cosmodog.actions.loweringgate;

import java.io.Serial;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.collect.Sets;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.dynamicpieces.Gate;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

/**
 * Represents the action of lowering gates in a specific region.
 * (Currently, the only gate is in the alien base. The action is registered in UpdateAlienBaseGateSequenceAction.
 * The latter is not an Async Action but a rule action.)
 * <p>
 * Gates are implemented as dynamic pieces. They have their state which can be updated.
 * The state defines not only whether the gate is open or closed but also the states between (lowering phases).
 * <p>
 * This action is a fixed length asynchronous action.
 */
public class LowerGateAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -8462906572353901070L;

	/**
	 * The name of the region in which the gates are located.
	 * The region will be taken from the map as a TiledObject. Only gates in this region will be lowered.
	 */
	private final String regionName;

	/**
	 * Collection to store all gates that will be found in the region.
	 */
	private final Set<Gate> gatesInRegion = Sets.newHashSet();

	/**
	 * Constructor.
	 *
	 * @param duration The duration of the action.
	 * @param regionName The name of the region (object on the TiledMap) in which the gates are located.
	 */
	public LowerGateAction(int duration, String regionName) {
		super(duration);
		this.regionName = regionName;
	}

	/**
	 * Returns the region name in which gates should be found.
	 *
	 * @return The name of the region where gates should be found.
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * Executed when the action is triggered.
	 * <p>
	 * This method finds all gates in the region with the given name (object on the TiledMap)
	 * and stores them in a collection to be updated in the onUpdate and onEnd methods later.
	 */
	@Override
	public void onTrigger() {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		TiledObjectGroup regions = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_REGIONS);
		TiledObject region = regions.getObjects().get(regionName);
		Collection<DynamicPiece> gates = map.getDynamicPieces().get(Gate.class);
        for (DynamicPiece dynamicPiece : gates) {
            Gate gate = (Gate) dynamicPiece;
			int tileLength = TileUtils.tileLengthSupplier.get();
            if (RegionUtils.pieceInRegion(gate, map.getMapType(), region)) {
                gatesInRegion.add(gate);
            }
        }
	}

	/**
	 * Updates the action based on the passed time.
	 * <p>
	 * First, the action completion percentage is calculated based on the passed time and overall duration. (0.0..1.0).
	 * Then, the state of all gates (found in the onTrigger method) is updated.
	 * This state shows the current phase of lowering the gate so the renderer can render the corresponding
	 * animation phases. The more the completion percentage, the more the gate is lowered until it is down completely.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
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

	/**
	 * This method is called when the action is finished.
	 * It updates the state of all gates in the region to the lowered state.
	 * This is necessary because the onUpdate method might not reach 1.0 exactly due to floating point precision.
	 */
	@Override
	public void onEnd() {
		for (Gate gate : gatesInRegion) {
			gate.setState(Gate.STATE_LOWERED);
		}
	}
}
