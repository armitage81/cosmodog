package antonafanasjew.cosmodog.model;

public class CollectibleGoodie extends Collectible {

	private static final long serialVersionUID = -1914902298661514996L;

	public static enum GoodieType {
		infobit(100),
		infobyte(500),
		infobank(2500),
		insight(10000),
		software(3000),
		chart(500),
		supplies(200),
		medipack(200),
		soulessence(500),
		armor(500),
		bottle(1000),
		foodcompartment(1000);

		private int scorePoints;
		
		private GoodieType(int scorePoints) {
			this.scorePoints = scorePoints;
		}
		
		public int getScorePoints() {
			return scorePoints;
		}
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
