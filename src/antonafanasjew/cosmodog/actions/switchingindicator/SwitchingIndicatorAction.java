package antonafanasjew.cosmodog.actions.switchingindicator;

import java.io.Serial;
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
import antonafanasjew.cosmodog.model.dynamicpieces.BinaryIndicator;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

import com.google.common.collect.Sets;

/**
 * Represents a switch in (possibly multiple) binary indicators such as the traffic light with green and red statuses
 * in the alien base.
 * <p>
 * Is initialized with the name of a region on a map (object in the layer "regions") and a boolean value that indicates
 * whether the indicators in the region should be switched on or off.
 * <p>
 * Searches for all binary indicators (they are dynamic pieces with a specific type) in the region and switches them
 * on or off at exactly half of the action duration.
 * <p>
 * This is a fixed length action.
 * TODO: Theoretically, it could happen that a huge time span happens between updates. This could lead to the switches not executed. Add onEnd method that does the same as what onUpdate does at half the duration.
 *
 */
public class SwitchingIndicatorAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -8462906572353901070L;

	/**
	 * The name of the region on the map that will be considered when searching binary indicators.
	 */
	private final String regionName;

	/**
	 * Only one switch is allowed per action. This flag is set to true when the switch has been executed at
	 * half of the action duration.
	 */
	private boolean switchedAlready = false;

	/**
	 * Holds the found binary indicators (dynamic pieces) throughout the action.
	 */
	private final Set<BinaryIndicator> indicatorsInRegion = Sets.newHashSet();

	/**
	 * Indicates whether the indicators in the region should be switched on or off.
	 */
	private final boolean onNotOff;

	/**
	 * Initializes the action with the duration, the name of the region, and the boolean value that indicates whether
	 * the indicators in the region should be switched on or off.
	 *
	 * @param duration The duration of the action.
	 * @param regionName The name of the region on the map that will be considered when searching binary indicators.
	 * @param onNotOff Indicates whether the indicators in the region should be switched on or off.
	 */
	public SwitchingIndicatorAction(int duration, String regionName, boolean onNotOff) {
		super(duration);
		this.regionName = regionName;
		this.onNotOff = onNotOff;
	}

	/**
	 * Returns the name of the region on the map that will be considered when searching binary indicators.
	 *
	 * @return The name of the region on the map that will be considered when searching binary indicators.
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * Collects all binary indicators (dynamic pieces) in the region and stores them in the set indicatorsInRegion
	 * to be updated later during the action execution.
	 * <p>
	 * Uses the map to get the object group "regions" and the object with the name regionName.
	 * <p>
	 * Retrieves all binary indicators from the map and checks if they are in the region.
	 */
	@Override
	public void onTrigger() {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		TiledObjectGroup regions = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_REGIONS);
		TiledObject region = regions.getObjects().get(regionName);
		Collection<DynamicPiece> binaryIndicators = map.getDynamicPieces().get(BinaryIndicator.class);
        for (DynamicPiece indicator : binaryIndicators) {
            BinaryIndicator binaryIndicator = (BinaryIndicator) indicator;
            if (RegionUtils.pieceInRegion(binaryIndicator, region, map.getTileWidth(), map.getTileHeight())) {
                indicatorsInRegion.add(binaryIndicator);
            }
        }
	}

	/**
	 * At exactly half of the action duration, the indicators in the region are switched on or off. A specific sound
	 * is played when the switch is executed. The switching flag is set to avoid multiple switches for
	 * the same piece.
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
		
		for (BinaryIndicator binaryIndicator : indicatorsInRegion) {
			
			if (!switchedAlready) {
				if (actionPercentage >= 0.5) {
					binaryIndicator.setState(onNotOff ? BinaryIndicator.STATE_TRUE : BinaryIndicator.STATE_FALSE);
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SECRET_FOUND).play();
					switchedAlready = true;
				}
			}
		}
	}
}
