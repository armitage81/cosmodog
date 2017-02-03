package antonafanasjew.cosmodog.util;

import java.util.List;

import antonafanasjew.cosmodog.notifications.Notification;

import com.google.common.collect.Lists;

public class NotificationUtils {

	private static final int DEFAULT_NOTIFICATION_DURATION = 6000;
	private static final int FOUND_BOAT_TEXT_DURATION = 6000;
	private static final int FOUND_DYNAMITE_TEXT_DURATION = 6000;

	private static final List<String> FOUND_DYNAMITE_TEXTS = Lists.newArrayList();
	
	static {
		FOUND_DYNAMITE_TEXTS.add("Alisa: Dynamite!? Be cautious with it, sweetie, or you will lose an arm... and me with it.");
	}
	
	private static final List<String> FOUND_BOAT_TEXTS = Lists.newArrayList();
	static {
		FOUND_BOAT_TEXTS.add("Alisa: A boat! Don't tell me you are gonna use it! I HATE water!");
	}
	
	private static final List<String> FOUND_MEDIPACK_TEXTS = Lists.newArrayList();
	static {
		FOUND_MEDIPACK_TEXTS.add("Alisa: This medipack will heal you in no time.");
		FOUND_MEDIPACK_TEXTS.add("Alisa: Health bar restored!");
		FOUND_MEDIPACK_TEXTS.add("Alisa: You're as good as new.");
		FOUND_MEDIPACK_TEXTS.add("Alisa: Finally! I've already started to worry about you.");
		FOUND_MEDIPACK_TEXTS.add("Alisa: Take the red pill.");
		FOUND_MEDIPACK_TEXTS.add("Alisa: Don't forget the flesh wound.");
		FOUND_MEDIPACK_TEXTS.add("Alisa: Are there some antidepressants?");
		FOUND_MEDIPACK_TEXTS.add("Alisa: Do you think you could charge me with the defibrillator?");
		FOUND_MEDIPACK_TEXTS.add("Alisa: Be cautious with the nail!");
		FOUND_MEDIPACK_TEXTS.add("Alisa: Do you know this one? \"Doctor, everyone ignores me.\". \"Next!\"");
	}
	
	private static final List<String> FOUND_SUPPLY_TEXTS = Lists.newArrayList();
	static {
		FOUND_SUPPLY_TEXTS.add("Alisa: How did it taste?");
		FOUND_SUPPLY_TEXTS.add("Alisa: I wonder how the eating process feels like.");
		FOUND_SUPPLY_TEXTS.add("Alisa: Bon appetite!");
		FOUND_SUPPLY_TEXTS.add("Alisa: Are there olives in the ration?");
		FOUND_SUPPLY_TEXTS.add("Alisa: Chew thoroughly, boss.");
		FOUND_SUPPLY_TEXTS.add("Alisa: Finally, some food!");
		FOUND_SUPPLY_TEXTS.add("Alisa: Yeah, hunger killer!");
		FOUND_SUPPLY_TEXTS.add("Alisa: Yammy!");
		FOUND_SUPPLY_TEXTS.add("Alisa: Finally! I could eat a whole cow! (Actually, I coudn't.)");
		FOUND_SUPPLY_TEXTS.add("Alisa: You sure, it has enough vitamines?");
	}

	private static final List<String> FOUND_WATER_TEXTS = Lists.newArrayList();

	static {
		FOUND_WATER_TEXTS.add("Alisa: Refreshing, isn't it?");
		FOUND_WATER_TEXTS.add("Alisa: That's H2O how I like it!");
		FOUND_WATER_TEXTS.add("Alisa: Ah, water, the basis of life ...carbon-based life, at least.");
		FOUND_WATER_TEXTS.add("Alisa: Be cautious, it might be polluted.");
		FOUND_WATER_TEXTS.add("Alisa: Better than those chemical soda drinks they serve back home, isn't it?");
		FOUND_WATER_TEXTS.add("Alisa: Die, thirst, die!");
		FOUND_WATER_TEXTS.add("Alisa: Do you think, they will invent robots who can drink, one day?");
		FOUND_WATER_TEXTS.add("Alisa: Cold and clear and refreshing... like you mom! (No, this one didn't work.)");
		FOUND_WATER_TEXTS.add("Alisa: Hey, not that fast. I'm water-proof but my aquaphoby doesn't know it");
		FOUND_WATER_TEXTS.add("Alisa: Time for drinks!");
	}

	private static final List<String> ENTERED_VEHICLE_TEXTS = Lists.newArrayList();

	static {
		ENTERED_VEHICLE_TEXTS.add("Alisa: The key is in the sun visor.");
		ENTERED_VEHICLE_TEXTS.add("Alisa: Fasten your seat belt.");
		ENTERED_VEHICLE_TEXTS.add("Alisa: Do you actually have a driver's license?");
		ENTERED_VEHICLE_TEXTS.add("Alisa: How fast can it go?");
		ENTERED_VEHICLE_TEXTS.add("Alisa: The Cosmodog woudn't have a chance against this baby.");
		ENTERED_VEHICLE_TEXTS.add("Alisa: I wonder what happened to it's owner.");
		ENTERED_VEHICLE_TEXTS.add("Alisa: I like the smell of gasoline... not!");
		ENTERED_VEHICLE_TEXTS.add("Alisa: Are you taking me for a ride, boss?");
		ENTERED_VEHICLE_TEXTS.add("Alisa: 3.. 2.. 1.. Go!");
		ENTERED_VEHICLE_TEXTS.add("Alisa: It's still working!");
	}
	
	private static final List<String> ENTERED_VEHICLE_WITHOUT_FUEL_TEXTS = Lists.newArrayList();
	static {
		ENTERED_VEHICLE_WITHOUT_FUEL_TEXTS.add("Alisa: It won't start without fuel.");
		ENTERED_VEHICLE_WITHOUT_FUEL_TEXTS.add("Alisa: This one's ran out of fuel.");
		ENTERED_VEHICLE_WITHOUT_FUEL_TEXTS.add("Alisa: No chance, the tank is empty.");
		ENTERED_VEHICLE_WITHOUT_FUEL_TEXTS.add("Alisa: This baby doesn't go anywhere.");
		ENTERED_VEHICLE_WITHOUT_FUEL_TEXTS.add("Alisa: It has no gasoline.");
	}

	
	private static int foundSupplyIndex = 0;
	private static int foundWaterIndex = 0;
	private static int foundMedipackIndex = 0;
	private static int enteredVehicleIndex = 0;
	private static int enteredVehicleWithoutFuelIndex = 0;

	public static Notification foundBoat() {
		return Notification.fromTextAndDuration(FOUND_BOAT_TEXTS.get(0), FOUND_BOAT_TEXT_DURATION);
	}
	
	public static Notification foundDynamite() {
		return Notification.fromTextAndDuration(FOUND_DYNAMITE_TEXTS.get(0), FOUND_DYNAMITE_TEXT_DURATION);
	}
	
	public static Notification foundSupply() {
		String s = FOUND_SUPPLY_TEXTS.get(foundSupplyIndex++ % FOUND_SUPPLY_TEXTS.size());
		return Notification.fromTextAndDuration(s, DEFAULT_NOTIFICATION_DURATION);
	}

	public static Notification foundWater() {
		String s = FOUND_WATER_TEXTS.get(foundWaterIndex++ % FOUND_WATER_TEXTS.size());
		return Notification.fromTextAndDuration(s, DEFAULT_NOTIFICATION_DURATION);
	}
	
	public static Notification foundMedipack() {
		String s = FOUND_MEDIPACK_TEXTS.get(foundMedipackIndex++ % FOUND_MEDIPACK_TEXTS.size());
		return Notification.fromTextAndDuration(s, DEFAULT_NOTIFICATION_DURATION);
	}

	public static Notification foundVehicle() {
		String s = ENTERED_VEHICLE_TEXTS.get(enteredVehicleIndex++ % ENTERED_VEHICLE_TEXTS.size());
		return Notification.fromTextAndDuration(s, DEFAULT_NOTIFICATION_DURATION);
	}
	
	public static Notification foundPlatform() {
		String s = "Alisa: Full steam ahead!";
		return Notification.fromTextAndDuration(s, DEFAULT_NOTIFICATION_DURATION);
	}
	
	public static Notification foundVehicleWithoutFuel() {
		String s = ENTERED_VEHICLE_WITHOUT_FUEL_TEXTS.get(enteredVehicleWithoutFuelIndex++ % ENTERED_VEHICLE_WITHOUT_FUEL_TEXTS.size());
		return Notification.fromTextAndDuration(s, DEFAULT_NOTIFICATION_DURATION);
	}

	public static Notification foundGeigerzaehler() {
		return Notification.fromTextAndDuration("Alisa: Some call it the Geiger counter, but I prefer \"The Heart Beat of Broken Atoms\".", DEFAULT_NOTIFICATION_DURATION);
	}
	
	public static Notification foundSupplyTracker() {
		return Notification.fromTextAndDuration("Alisa: Hey, that's the supply tracker. It shows you the direction to the next supply box.", DEFAULT_NOTIFICATION_DURATION);
	}
	
	public static Notification foundBinoculars() {
		return Notification.fromTextAndDuration("Alisa: If everything looks more distant, then you're holding it the other way around.", DEFAULT_NOTIFICATION_DURATION);
	}
	
	public static Notification foundJacket() {
		return Notification.fromTextAndDuration("Alisa: What a fine coat. I bet you look GREAT in it, sweetie! Also, you won't die in the snow.", DEFAULT_NOTIFICATION_DURATION);
	}
	
	public static Notification foundAntidote() {
		return Notification.fromTextAndDuration("Alisa: I always hated suringes. They remind me on microchip production lasers. Child trauma, you could say.", DEFAULT_NOTIFICATION_DURATION);
	}
	
	public static Notification foundSki() {
		return Notification.fromTextAndDuration("Alisa: I would prefer a snow mobile but this will do it, as well.", DEFAULT_NOTIFICATION_DURATION);
	}
	
}
