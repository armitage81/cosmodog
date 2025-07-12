package antonafanasjew.cosmodog.model.inventory;

public enum InventoryItemType {

	VEHICLE(false),
	PLATFORM(false),
	BOAT(true),
	DYNAMITE(true),
	FUEL_TANK(false),
	INSIGHT(false),
	SOFTWARE(false),
	CHART(false),
	KEY_RING(false),
	GEIGERZAEHLER(true),
	SUPPLYTRACKER(true),
	BINOCULARS(true),
	SKI(true),
	JACKET(true),
	ANTIDOTE(true),
	MINEDETECTOR(true),
	MINEDEACTIVATIONCODES(true),
	PICK(true),
	MACHETE(true),
	AXE(true),
	GOODIE(false),
	AMMO(false),
	RADIOACTIVESUIT(true),
	ARCHEOLOGISTS_JOURNAL(true),
	WEAPON_FIRMWARE_UPGRADE(true),
	NUTRIENTS(true),
	PORTAL_GUN(true),
	DEBUGGER(false);

	private boolean representsTool;

	private InventoryItemType(boolean representsTool) {
		this.representsTool = representsTool;
	}

	public boolean isRepresentsTool() {
		return representsTool;
	}
}
