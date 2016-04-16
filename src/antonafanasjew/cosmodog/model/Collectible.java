package antonafanasjew.cosmodog.model;

public class Collectible extends Piece {
	
	private static final long serialVersionUID = -7602342357959812236L;

//	public static final String COLLECTIBLE_TYPE_WEAPON_PISTOL = "pistol";
//	public static final String COLLECTIBLE_TYPE_WEAPON_SHOTGUN = "shotgun";
//	public static final String COLLECTIBLE_TYPE_WEAPON_RIFLE = "rifle";
//	public static final String COLLECTIBLE_TYPE_WEAPON_MACHINEGUN = "machinegun";
//	public static final String COLLECTIBLE_TYPE_WEAPON_ROCKETLAUNCHER = "rocketlauncher";
	
	public static enum CollectibleType {
		GOODIE,
		TOOL,
		WEAPON,
		AMMO
	}
	

	private CollectibleType collectibleType;

	public Collectible(CollectibleType collectibleType) {
		this.collectibleType = collectibleType;
	}

	public CollectibleType getCollectibleType() {
		return collectibleType;
	}

}
