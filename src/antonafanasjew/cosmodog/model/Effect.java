package antonafanasjew.cosmodog.model;

public class Effect extends Piece {

	private static final long serialVersionUID = -7602342357959812236L;

	public static final String EFFECT_TYPE_FIRE = "fire";
	public static final String EFFECT_TYPE_SMOKE = "smoke";
	public static final String EFFECT_TYPE_ELECTRICITY = "electricity";
	public static final String EFFECT_TYPE_BIRDS = "birds";
	public static final String EFFECT_TYPE_ENERGYWALL = "energywall";

	private String effectType;

	public Effect(String effectType) {
		this.effectType = effectType;
	}

	public String getEffectType() {
		return effectType;
	}

}
