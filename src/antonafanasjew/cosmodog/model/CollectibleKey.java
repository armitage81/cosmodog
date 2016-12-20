package antonafanasjew.cosmodog.model;

import antonafanasjew.cosmodog.model.upgrades.Key;


public class CollectibleKey extends Collectible {

	private static final long serialVersionUID = 8589176730233780875L;
	
	private Key key;

	public CollectibleKey(Key key) {
		super(Collectible.CollectibleType.KEY);
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}






}
