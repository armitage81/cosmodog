package antonafanasjew.cosmodog.calendar.listeners;

import antonafanasjew.cosmodog.calendar.AbstractPlanetaryCalendarListener;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Calendar listener that consumes vehicle fuel for each time update on the calendar.
 */
public class FuelConsumer extends AbstractPlanetaryCalendarListener {


	private static final long serialVersionUID = 5255066428594503283L;

	/**
	 * Consumes fuel if the player is in a vehicle and the fuel feature is activated depending on the passed time.
	 * 
	 * Take care: This listener assumes that the vehicle is moving with constant speed greater zero, so it won't make sense to use it
	 * in case the time can be passed while the car is staying on the same location (kind of waiting function). In such a case, fuel
	 * would be consumed nevertheless. 
	 *  
	 */
	@Override
	protected void timePassedInternal(long fromTimeInMinutes, int noOfMinutes) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_FUEL, new Runnable() {
			@Override
			public void run() {
				Player player = ApplicationContextUtils.getPlayer();				
				if (player.getInventory().hasVehicle()) {
					int timeUnits = noOfMinutes / Constants.MINIMAL_TIME_UNIT_IN_MINUTES;				

					VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItem.INVENTORY_ITEM_VEHICLE);
					Vehicle vehicle = vehicleInventoryItem.getVehicle();

					vehicle.decreaseFuel(timeUnits);
				}
			}
		});
	}
	
}
