package antonafanasjew.cosmodog.model.inventory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.model.upgrades.WeaponAmmunitionListener;

/**
 * Represents player's arsenal.
 * As opposite to the other items, the arsenal is just a container
 * for weapons.
 */
public class Arsenal implements WeaponAmmunitionListener, Serializable {

	
	private static final long serialVersionUID = 7890272006234765988L;

	private Map<WeaponType, Weapon> weapons = Maps.newHashMap();
	
	private List<WeaponType> weaponsOrder = Lists.newArrayList(WeaponType.FISTS, WeaponType.PISTOL, WeaponType.SHOTGUN, WeaponType.RIFLE, WeaponType.MACHINEGUN, WeaponType.RPG);
	
	private WeaponType selectedWeaponType;
	
	/**
	 * Constructor. Initializes the arsenal with the dummy weapon 'Fists'
	 */
	public Arsenal() {
		this.weapons.put(WeaponType.FISTS, new Weapon(WeaponType.FISTS));
	}

	/**
	 * Adds a weapon to the arsenal. Only one weapon of each type can be stored in the arsenal.
	 * @param weapon Weapon to be added.
	 */
	public void addWeaponToArsenal(Weapon weapon) {
		this.weapons.put(weapon.getWeaponType(), weapon);
		weapon.addListener(this);
	}

	/**
	 * Returns a copy of the weapons map in this arsenal.
	 * @return Copy of the map of weapons by weapon type.
	 */
	public Map<WeaponType, Weapon> getWeaponsCopy() {
		return Maps.newHashMap(weapons);
	}
	
	/**
	 * Returns the order of weapons. As weapons are just stored in a map
	 * by weapon type, there needs to be an order when listing weapons from the arsenal.
	 * This is the way to obtain it.
	 * @return Order for listing weapons from the arsenal.
	 */
	public List<WeaponType> getWeaponsOrder() {
		return weaponsOrder;
	}

	
	/**
	 * Returns the selected weapon type. 
	 * If nothing is selected (f.i. because a new weapon was collected recently) 
	 * it returns fists as first weapon type.
	 */
	public WeaponType getSelectedWeaponType() {
		
		if (selectedWeaponType == null) {
			selectFirstAvailableWeaponTypeForward(0);
		}
		
		return selectedWeaponType;
	}

	private void selectFirstAvailableWeaponTypeForward(int startingIndex) {
		boolean firstLoop = true;
		for (int i = startingIndex; firstLoop || i != startingIndex; i = (i + 1) % weaponsOrder.size()) {
			WeaponType weaponType = weaponsOrder.get(i);
			Weapon weapon = weapons.get(weaponType);
			if (weapon != null && (weapon.getAmmunition() > 0 || weapon.getWeaponType().infiniteAmmo())) {
				selectedWeaponType = weaponType;
				break;
			}
			firstLoop = false;
		}
	}
	
	private void selectFirstAvailableWeaponTypeBackward(int startingIndex) {
		boolean firstLoop = true;
		for (int i = startingIndex; firstLoop || i != startingIndex; i = (i - 1) % weaponsOrder.size()) {
			WeaponType weaponType = weaponsOrder.get(i);
			Weapon weapon = weapons.get(weaponType);
			if (weapon != null && (weapon.getAmmunition() > 0 || weapon.getWeaponType().infiniteAmmo())) {
				selectedWeaponType = weaponType;
				break;
			}
			firstLoop = false;
		}
	}
	
	/**
	 * Selects the next available weapon type in the arsenal related to the currently
	 * selected weapon type. The order of listed weapons is defined in this class.
	 * If the last weapon is currently selected, selects fists as the default weapon type.
	 * If Fists are selected, selects the first collected weapon.
	 */
	public void selectNextWeaponType() {
		WeaponType weaponType = getSelectedWeaponType();
		
		if (weaponType != null) {
			int index = weaponsOrder.indexOf(weaponType);
			
			int nextIndex = (index + 1) % weaponsOrder.size();
			
			selectFirstAvailableWeaponTypeForward(nextIndex);
		}
		
	}

	public void selectWeaponType(WeaponType weaponType) {
		WeaponType selectedWeaponType = getSelectedWeaponType();
		while (selectedWeaponType != weaponType) {
			selectNextWeaponType();
			selectedWeaponType = getSelectedWeaponType();
		}
	}

	/**
	 * Selects the previous available weapon type in the arsenal related to the currently
	 * selected weapon type. The order of listed weapons is defined in this class.
	 * If the first collected weapon is currently selected, selects fists as the default weapon type.
	 * If Fists are selected, selects the last collected weapon.
	 */
	public void selectPreviousWeaponType() {
		WeaponType weaponType = getSelectedWeaponType();
		
		if (weaponType != null) {
			int index = weaponsOrder.indexOf(weaponType);
			
			int previousIndex = (index - 1);
			
			if (previousIndex < 0 ) {
				previousIndex = weaponsOrder.size() - 1;
			}
			
			selectFirstAvailableWeaponTypeBackward(previousIndex);
		}
		
	}

	/**
	 * In case the ammo of a weapon is empty (the weapon is pef def the selected weapon),
	 * then deselects it and selects the first available weapon in the arsenal.
	 * If no collected weapons are available, selects Fists as default weapon.
	 */
	@Override
	public void onAmmunitionChange(Weapon weapon, int ammunitionBefore) {
		if (weapon.getWeaponType().infiniteAmmo() == false && weapon.getAmmunition() <= 0) {
			this.selectFirstAvailableWeaponTypeBackward(0);
		}
	}
	
}
