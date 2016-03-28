package antonafanasjew.cosmodog.domains;

/**
 * Domain for weapon types. Defines the damage of a weapon.
 *
 */
public enum WeaponType {

	/**
	 * Machine gun.
	 */
	MACHINEGUN(1),
	
	/**
	 * Double gun.
	 */
	DOUBLEGUN(3),
	
	/**
	 * Pistol.
	 */
	PISTOL(1);
	
	private int basicDamage;
	
	private WeaponType(int basicDamage) {
		this.basicDamage = basicDamage;
	}

	/**
	 * Returns weapon's basic damage.
	 * @return Basic damage of the weapon type.
	 */
	public int getBasicDamage() {
		return basicDamage;
	}

}
