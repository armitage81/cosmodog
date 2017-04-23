package antonafanasjew.cosmodog.model.upgrades;

import java.io.Serializable;
import java.util.List;

import antonafanasjew.cosmodog.domains.WeaponType;

import com.google.common.collect.Lists;

public class Weapon implements Serializable {

	private static final long serialVersionUID = 7316365846677172148L;
	private WeaponType weaponType;
	private int ammunition;
	private int maxAmmunition; //Normally, this value is defined by the weapon type. But it also can be increased by the weapon upgrade (if found a second instance of the same weapon)
	
	private List<WeaponAmmunitionListener> listeners = Lists.newArrayList();
	
	public Weapon(WeaponType weaponType) {
		this(weaponType, weaponType.getMaxAmmo());
	}
	
	public Weapon(WeaponType weaponType, int ammunition) {
		super();
		this.weaponType = weaponType;
		this.ammunition = ammunition;
		this.maxAmmunition = this.weaponType.getMaxAmmo();
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}

	public int getAmmunition() {
		return ammunition;
	}

	public void setAmmunition(int ammunition) {
		
		int oldAmmunition = this.ammunition;
		this.ammunition = ammunition;
		for (WeaponAmmunitionListener listener : listeners) {
			listener.onAmmunitionChange(this, oldAmmunition);
		}
	} 
	
	public void reduceAmmunition(int n) {
		if (n < 1) {
			n = 0;
		}
		
		int currentAmmunition = this.ammunition;
		currentAmmunition -= n;
		
		if (currentAmmunition < 0) {
			currentAmmunition = 0;
		}
		
		setAmmunition(currentAmmunition);
	}

	public List<WeaponAmmunitionListener> getListeners() {
		return listeners;
	}

	public void addListener(WeaponAmmunitionListener listener) {
		this.listeners.add(listener);
	}
	
	public int getMaxAmmunition() {
		return maxAmmunition;
	}
	
	public void upgrade() {
		this.maxAmmunition = this.maxAmmunition + 1;
		setAmmunition(this.maxAmmunition);
	}
	
}
