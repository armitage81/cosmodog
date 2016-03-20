package antonafanasjew.cosmodog.domains;

public enum ArmorType {

	HEAVY(2),
	LIGHT(0);
	
	private int damageAbsorption;
	
	private ArmorType(int damageAbsorption) {
		this.damageAbsorption = damageAbsorption;
	}

	public int getDamageAbsorption() {
		return damageAbsorption;
	}

}
