package antonafanasjew.cosmodog.model;

public class CollectibleGoodie extends Collectible {

	private static final long serialVersionUID = -1914902298661514996L;

	public static enum GoodieType {
		infobit,
		insight,
		supplies,
		medipack,
		soulessence,
		armor
	}
	
	private GoodieType goodieType;
	
	public CollectibleGoodie(GoodieType goodieType) {
		super(Collectible.CollectibleType.GOODIE);
		this.goodieType = goodieType;
	}

	public GoodieType getGoodieType() {
		return goodieType;
	}

}
