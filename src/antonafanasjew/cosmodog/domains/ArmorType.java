package antonafanasjew.cosmodog.domains;

/**
 * Domain of armor types. Defines the damage protection of an armor type.
 */
public enum ArmorType {

	/**
	 * Heavy armor.
	 */
	HEAVY(2),
	
	/**
	 * Light armor.
	 */
	LIGHT(0);
	
	private int damageAbsorption;
	
	private ArmorType(int damageAbsorption) {
		this.damageAbsorption = damageAbsorption;
	}

	/**
	 * Returns the damage absorption of the armor type.
	 */
	public int getDamageAbsorption() {
		return damageAbsorption;
	}

}
