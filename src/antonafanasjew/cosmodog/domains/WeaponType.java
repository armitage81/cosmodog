package antonafanasjew.cosmodog.domains;

/**
 * Domain for weapon types. Defines the damage of a weapon.
 *
 */
public enum WeaponType {
	
	/**
	 * Fists. This weapon type won't be rendered in the arsenal.
	 */
	FISTS(1, 0),
	
	/**
	 * Double gun.
	 */
	DOUBLEGUN(3, 20),
	
	/**
	 * Pistol.
	 */
	PISTOL(10, 10),
	
	SHOTGUN(20, 5),
	
	RIFLE(30, 20),
	
	RPG(50, 3),
	
	/**
	 * Machine gun.
	 */
	MACHINEGUN(100, 1)
	;
	
	private int basicDamage;
	private int maxAmmo;
	
	private WeaponType(int basicDamage, int maxAmmo) {
		this.basicDamage = basicDamage;
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
	 * Returns the maximal possible ammunition.
	 * @return maximal ammunition.
	 */
	public int getMaxAmmo() {
		return maxAmmo;
	}

	public boolean infiniteAmmo() {
		return this.maxAmmo == 0;
	}
	
}
