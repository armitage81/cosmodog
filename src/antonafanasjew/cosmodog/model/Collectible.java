package antonafanasjew.cosmodog.model;

public class Collectible extends Piece {
	
	private static final long serialVersionUID = -7602342357959812236L;

	public static enum CollectibleType {
		BONUS,
		GOODIE,
		TOOL,
		WEAPON,
		AMMO,
		KEY,
		LOG,
		COMPOSED
	}
	

	private CollectibleType collectibleType;

	public Collectible(CollectibleType collectibleType) {
		this.collectibleType = collectibleType;
	}

	public CollectibleType getCollectibleType() {
		return collectibleType;
	}

}
