package antonafanasjew.cosmodog.model.inventory;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.model.upgrades.WeaponAmmunitionListener;


public class ArsenalInventoryItem extends InventoryItem implements WeaponAmmunitionListener {

	
	private static final long serialVersionUID = -1043098798358662237L;

	
	private Map<WeaponType, Weapon> weapons = Maps.newHashMap();
	
	private List<WeaponType> weaponsOrder = Lists.newArrayList(WeaponType.FISTS, WeaponType.PISTOL, WeaponType.SHOTGUN, WeaponType.RIFLE, WeaponType.MACHINEGUN, WeaponType.RPG);
	
	private WeaponType selectedWeaponType;
	
	public ArsenalInventoryItem() {
		super(InventoryItem.INVENTORY_ITEM_ARSENAL);
		this.weapons.put(WeaponType.FISTS, new Weapon(WeaponType.FISTS));
	}

	public void addWeaponToArsenal(Weapon weapon) {
		this.weapons.put(weapon.getWeaponType(), weapon);
		weapon.addListener(this);
	}

	public Map<WeaponType, Weapon> getWeaponsCopy() {
		return Maps.newHashMap(weapons);
	}
	
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
	
	public void selectNextWeaponType() {
		WeaponType weaponType = getSelectedWeaponType();
		
		if (weaponType != null) {
			int index = weaponsOrder.indexOf(weaponType);
			
			int nextIndex = (index + 1) % weaponsOrder.size();
			
			selectFirstAvailableWeaponTypeForward(nextIndex);
		}
		
	}
	
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

	@Override
	public void onAmmunitionChange(Weapon weapon, int ammunitionBefore) {
		if (weapon.getWeaponType().infiniteAmmo() == false && weapon.getAmmunition() <= 0) {
			this.selectFirstAvailableWeaponTypeBackward(0);
		}
	}
	
}
