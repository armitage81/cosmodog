package antonafanasjew.cosmodog.domains;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * Domain for weapon types. Defines the damage of a weapon.
 *
 */
public enum WeaponType {
	
		
	FISTS(5, damageMapFromIntArray(5,5,5,5,5,5), 0),
	PISTOL(5, damageMapFromIntArray(15,10,10,10,5,5), 10),
	SHOTGUN(5, damageMapFromIntArray(50,20,15,10,10,5), 5),
	RIFLE(5, damageMapFromIntArray(40,30,20,15,5,5), 20),
	MACHINEGUN(5, damageMapFromIntArray(50,60,30,20,15,10), 1),
	RPG(5, damageMapFromIntArray(5,5,5,20,50,50), 3),

	
	ENEMY_DOUBLEGUN(4, 0),
	ENEMY_PISTOL(1, 0),
	ENEMY_MACHINEGUN(3, 0),
	ENEMY_TURRET(7, 0)
	
	;
	
	private Map<ArmorType, Integer> damages = Maps.newHashMap();
	
	private int basicDamage;
	
	private int maxAmmo;
	
	private WeaponType(int basicDamage, int maxAmmo) {
		this(basicDamage, Maps.newHashMap(), maxAmmo);  
	}
	
	private WeaponType(int basicDamage, Map<ArmorType, Integer> damages, int maxAmmo) {
		this.basicDamage = basicDamage;
		this.damages = damages;
		this.maxAmmo = maxAmmo;
	}

	/**
	 * Returns weapon's basic damage.
	 * @return Basic damage of the weapon type.
	 */
	public int getBasicDamage() {
		return basicDamage;
	}

	/**
	 * Returns a damage for a specific armor type or basic damage if not defined.
	 * @param armorType Armor type for which the damage should be returned.
	 * @return Damage versus given armor type.
	 */
	public int getDamage(ArmorType armorType) {
		if (damages.containsKey(armorType)) {
			return damages.get(armorType);
		} else {
			return basicDamage;
		}
	}
	
	/**
	 * Returns the maximal possible ammunition.
	 * @return maximal ammunition.
	 */
	public int getMaxAmmo() {
		return maxAmmo;
	}

	public boolean infiniteAmmo() {
		return this.maxAmmo == 0;
	}
	
	private static Map<ArmorType, Integer> damageMapFromIntArray(int... numbers) {
		Preconditions.checkArgument(numbers.length == ArmorType.values().length, "Numbers to not match the number of armor types. Added new one and forgot to adjust damages?");
		
		Map<ArmorType, Integer> retVal = Maps.newHashMap();
		
		for (int i = 0; i < ArmorType.values().length; i++) {
			ArmorType armorType = ArmorType.values()[i];
			retVal.put(armorType, numbers[i]);
		}
		
		return retVal;
	}
}




























