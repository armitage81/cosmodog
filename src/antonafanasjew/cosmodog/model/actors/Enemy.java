package antonafanasjew.cosmodog.model.actors;

import java.util.Set;

import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.sight.Sight;

import com.google.common.collect.Sets;

/**
 * Represents enemy characters.
 */
public class Enemy extends NpcActor {

	private static final long serialVersionUID = -3515008009264002679L;

	private UnitType unitType;
	private WeaponType weaponType;
	private ArmorType armorType;
	
	private Set<Sight> sights = Sets.newHashSet();
	
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
	 * Returns a set of sight objects, each of which is representing a sight cone.
	 * @return Set of sight objects representing enemies range of view.
	 */
	public Set<Sight> getSights() {
		return sights;
	}

}
