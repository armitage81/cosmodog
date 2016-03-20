package antonafanasjew.cosmodog.model;

public class Collectible extends Piece {

	private static final long serialVersionUID = -7602342357959812236L;

	public static final String COLLECTIBLE_TYPE_INFOBIT = "infobit";
	public static final String COLLECTIBLE_TYPE_INSIGHT = "insight";
	public static final String COLLECTIBLE_TYPE_SUPPLIES = "supplies";
	public static final String COLLECTIBLE_TYPE_MEDIPACK = "medipack";
	public static final String COLLECTIBLE_TYPE_SOULESSENCE = "soulessence";
	public static final String COLLECTIBLE_TYPE_ARMOR = "armor";
	public static final String COLLECTIBLE_TYPE_ITEM = "item";
	public static final String COLLECTIBLE_TYPE_FUEL = "fuel";


	private String collectibleType;

	public Collectible(String collectibleType) {
		this.collectibleType = collectibleType;
	}

	public String getCollectibleType() {
		return collectibleType;
	}

}
