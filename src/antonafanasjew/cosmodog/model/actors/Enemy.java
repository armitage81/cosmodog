package antonafanasjew.cosmodog.model.actors;

import java.util.Set;

import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;

import antonafanasjew.cosmodog.sight.Vision;
import com.google.common.collect.Sets;

/**
 * Represents enemy characters.
 */
public class Enemy extends NpcActor {

	public static final int MAX_ALERT_LEVEL = 5;
	
	private static final long serialVersionUID = -3515008009264002679L;

	private UnitType unitType;
	private WeaponType weaponType;
	private ArmorType armorType;
	
	private int alertLevel = 0;
	
	/*
	 * If true, this concrete enemy will not be active at night.
	 * Normally, this property will be taken from the corresponding home region property.
	 * It is there to simulate units that are 'sleeping' at night.
	 */
	private boolean activeAtDayTimeOnly = false;
	
	private Vision defaultVision = new Vision();
	private Vision nightVision = new Vision();
	private Vision stealthVision = new Vision();
	
	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}

	public ArmorType getArmorType() {
		return armorType;
	}

	public void setArmorType(ArmorType armorType) {
		this.armorType = armorType;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	public boolean isActiveAtDayTimeOnly() {
		return activeAtDayTimeOnly;
	}

	public void setActiveAtDayTimeOnly(boolean activeAtDayTimeOnly) {
		this.activeAtDayTimeOnly = activeAtDayTimeOnly;
	}
	
	@Override
	public String toString() {
		return this.unitType + " at (" + getPosition() + ")";
	}

	public Vision getDefaultVision() {
		return defaultVision;
	}

	public Vision getNightVision() {
		return nightVision;
	}

	public Vision getStealthVision() {
		return stealthVision;
	}

	public int getAlertLevel() {
		return alertLevel;
	}

	public void setAlertLevel(int alertLevel) {
		this.alertLevel = alertLevel;
	}
	
	public void increaseAlertLevelToMax() {
		this.alertLevel = MAX_ALERT_LEVEL;
	}
	
	public void reduceAlertLevel() {
		if (alertLevel > 0) {
			alertLevel--;
		}
	}

}
