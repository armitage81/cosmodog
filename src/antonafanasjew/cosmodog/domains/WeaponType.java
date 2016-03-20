package antonafanasjew.cosmodog.domains;

public enum WeaponType {
	
	MACHINEGUN(1),
	DOUBLEGUN(3),
	PISTOL(1);
	
	private int basicDamage;
	
	private WeaponType(int basicDamage) {
		this.basicDamage = basicDamage;
	}

	public int getBasicDamage() {
		return basicDamage;
	}

}
