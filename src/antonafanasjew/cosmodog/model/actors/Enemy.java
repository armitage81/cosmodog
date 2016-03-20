package antonafanasjew.cosmodog.model.actors;

import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;

/**
 * Represents enemy characters.
 */
public class Enemy extends NpcActor {

	private static final long serialVersionUID = -3515008009264002679L;

	private UnitType unitType;
	private WeaponType weaponType;
	private ArmorType armorType;
	
	private int sightRadius;

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

	@Override
	public String toString() {
		return this.unitType + " at (" + getPositionX() + "/" + getPositionY() + ")";
	}

	/**
	 * Returns the orthogonal range from which the enemy sees the player. 
	 * @return Orthogonal sight radius in tiles.
	 */
	public int getSightRadius() {
		return sightRadius;
	}

	/**
	 * Sets the sight radius.
	 * @param sightRadius Orthogonal sight radius in tiles.
	 */
	public void setSightRadius(int sightRadius) {
		this.sightRadius = sightRadius;
	}
	
}
